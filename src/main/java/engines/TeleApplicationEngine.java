package engines;

import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationUser;
import botApplications.teleApplicationCore.teleApplicationCommands.TeleCMDDiscordConnection;
import botApplications.teleApplicationCore.teleApplicationCommands.TeleCMDShini;
import botApplications.teleApplicationCore.teleApplicationListeners.MessageListener;
import botApplications.teleApplicationCore.TeleCommandHandler;
import botApplications.teleApplicationCore.TeleCommandParser;
import botApplications.teleApplicationCore.utils.TeleBotUtils;
import botApplications.teleApplicationCore.utils.TeleTextUtils;
import com.pengrad.telegrambot.TelegramBot;
import net.dv8tion.jda.core.entities.PrivateChannel;

import java.util.ArrayList;

public class TeleApplicationEngine {

    Engine engine;

    boolean botApplicationRunning = false;

    TelegramBot bot;

    TeleBotUtils botUtils;
    TeleCommandHandler commandHandler;
    TeleCommandParser commandParser;
    TeleTextUtils textUtils;

    ArrayList<RequestDiscordConnection> requestDiscordConnection = new ArrayList<>();
    ArrayList<String> discordConnections = new ArrayList<>();

    public TeleApplicationEngine(Engine engine) {
        this.engine = engine;
    }

    public void startBotApplication() {

        if(botApplicationRunning){
            System.out.println("[Telegram] Bot is already running!");
        } else {
            if(engine.getProperties().getTeleBotApplicationToken().equalsIgnoreCase("")||engine.getProperties().getTeleBotApplicationToken()==null){
                System.out.println("[Telegram] !Fatal Token error...bot cant loaded, maybe token never set?");
                return;
            }
            System.out.println("[Telegram] Bot start initialized");
            botApplicationRunning = true;
            botUtils = new TeleBotUtils(engine);
            commandHandler = new TeleCommandHandler();
            commandParser = new TeleCommandParser();
            textUtils = new TeleTextUtils(engine);

            try {
                bot = new TelegramBot(engine.getProperties().getTeleBotApplicationToken());
            } catch (Exception e){
                System.out.println("[Telegram] !Fatal Token error...bot cant loaded, maybe token never set?");
                botApplicationRunning = false;
                return;
            }
            addCommands();
            bot.setUpdatesListener(new MessageListener(engine));
            System.out.println("[Telegram] !Telegram Bot successfully logged in!");
        }
    }

    public void shutdown() {
        if(!botApplicationRunning){
            System.out.println("Bot can't shutdown cause never starts!");
            return;
        }
        System.out.println("[Telegram] Bot is shutting down...");
        botApplicationRunning = false;
        bot.removeGetUpdatesListener();
    }

    private void addCommands() {
        System.out.println("[Telegram] ~Add commands");
        commandHandler.createNewCommand("shini", new TeleCMDShini());
        commandHandler.createNewCommand("discord", new TeleCMDDiscordConnection());
    }

    public TelegramBot getBot() {
        return bot;
    }

    public TeleBotUtils getBotUtils() {
        return botUtils;
    }

    public TeleCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public TeleCommandParser getCommandParser() {
        return commandParser;
    }

    public TeleTextUtils getTextUtils() {
        return textUtils;
    }

    public void createNewDiscordConnectionRequest(String userid, long telegramId) {
        requestDiscordConnection.add(new RequestDiscordConnection(userid, telegramId));
    }

    public void confirmRequest(String userid, PrivateChannel channel) {
        RequestDiscordConnection current;
        RequestDiscordConnection request = null;
        for (int i = 0; i < requestDiscordConnection.size(); i++) {
            current = requestDiscordConnection.get(i);
            if(current.userID.equals(userid)){
                request = current;
                break;
            }
        }

        if(request != null){
            if(discordConnections.contains(userid)){
                engine.getDiscEngine().getTextUtils().sendWarining("Du wurdest anscheinend schon regestriert :3", channel);
            } else {
                DiscApplicationUser updateUser = engine.getDiscEngine().getFilesHandler().getUsers().get(userid);
                engine.getDiscEngine().getFilesHandler().getUsers().remove(updateUser);
                updateUser.setTelegramId(request.telegramId);
                engine.getDiscEngine().getFilesHandler().getUsers().put(updateUser.getUserId(), updateUser);
                requestDiscordConnection.remove(request);
                discordConnections.add(userid);
                engine.getDiscEngine().getTextUtils().sendSucces("Dein Telegram Account wurde verbunden!", channel);
                engine.getTeleApplicationEngine().getTextUtils().sendMessage(request.telegramId, "Erfolgreich Verbunden!");
            }
        } else {
            engine.getDiscEngine().getTextUtils().sendError("Du hast keinen Connectionrequest erstellt! Um mehr infos zu erhalten, schreibe " + engine.getProperties().getDiscBotApplicationPrefix() + "telegram help!", channel, false);
        }
    }

    public void removeDiscordConnection(String userId){
        discordConnections.remove(userId);
    }

    public void rebootBotApplication() {
        shutdown();
        startBotApplication();
    }

    public class RequestDiscordConnection{
        String userID;
        long telegramId;

        public RequestDiscordConnection(String userID, long telegramId) {
            this.userID = userID;
            this.telegramId = telegramId;
        }
    }

    public boolean discordConnectionContaisId(String id){
        return  discordConnections.contains(id);
    }

    public DiscApplicationUser getUserByTelegramId(long id) throws Exception {
        Object[] users = engine.getDiscEngine().getFilesHandler().getUsers().values().toArray();
        DiscApplicationUser user;
        for (int i = 0; i < users.length; i++) {
            user = (DiscApplicationUser) users[i];
            if(user.getTelegramId()==id){
                return user;
            }
        }
        throw new Exception("User doesnt exist");
    }

    public boolean discordRequestContainsId(String id){
        for (int i = 0; i < requestDiscordConnection.size(); i++) {
            if(requestDiscordConnection.get(i).userID.equals(id)){
                return true;
            }
        }
        return false;
    }
}