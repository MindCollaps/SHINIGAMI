package Engines;

import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationUser;
import BotApplications.TeleApplicationCore.TeleApplicationCommands.TeleCMDDiscordConnection;
import BotApplications.TeleApplicationCore.TeleApplicationCommands.TeleCMDShini;
import BotApplications.TeleApplicationCore.TeleApplicationListeners.MessageListener;
import BotApplications.TeleApplicationCore.TeleCommandHandler;
import BotApplications.TeleApplicationCore.TeleCommandParser;
import BotApplications.TeleApplicationCore.Utils.TeleBotUtils;
import BotApplications.TeleApplicationCore.Utils.TeleTextUtils;
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

        System.out.println("[Telegram] Bot start initialized");
        botApplicationRunning = true;
        botUtils = new TeleBotUtils(engine);
        commandHandler = new TeleCommandHandler();
        commandParser = new TeleCommandParser();
        textUtils = new TeleTextUtils(engine);

        addCommands();
        bot = new TelegramBot("663797335:AAHBlqwMce8t9LhM0IVoRD4-J33alZNXmB4");
        bot.setUpdatesListener(new MessageListener(engine));
        System.out.println("[Telegram] !Telegram Bot successfully logged in!");
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