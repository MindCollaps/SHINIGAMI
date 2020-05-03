package engines;

import botApplications.discApplicationCore.discApplicationCommands.*;
import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationFilesHandler;
import botApplications.discApplicationCore.discApplicationListeners.ServerJoinListener;
import botApplications.discApplicationCore.discApplicationListeners.ServerMessageListener;
import botApplications.discApplicationCore.DiscCommandHandler;
import botApplications.discApplicationCore.DiscCommandParser;
import botApplications.discApplicationCore.utils.DiscTextUtils;
import botApplications.discApplicationCore.utils.DiscUtilityBase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class DiscApplicationEngine {

    Engine engine;

    private JDABuilder builder;
    private JDA botJDA;

    private DiscCommandHandler discCommandHandler;
    private DiscCommandParser discCommandParser;

    private DiscTextUtils textUtils;
    private DiscUtilityBase utilityBase;
    private DiscApplicationFilesHandler filesHandler;

    boolean botApplicationRunning = false;
    boolean debugAi = false;

    public DiscApplicationEngine(Engine engine) {
        this.engine = engine;
        textUtils = new DiscTextUtils(engine);
        utilityBase = new DiscUtilityBase(engine);
        filesHandler = new DiscApplicationFilesHandler(engine);
    }

    public void startBotApplication() {
        if (botApplicationRunning) {
            System.out.println("[Discord] !!!Discord Bot is already running!!!");
            return;
        }
        if(engine.getProperties().getDiscBotApplicationToken().equalsIgnoreCase("")||engine.getProperties().getDiscBotApplicationToken()==null){
            System.out.println("[Discord] !!!Bot start failure!!!\nMaybe the token invalid");
            return;
        }
        System.out.println("[Discord] Bot start initialized");
        botApplicationRunning = true;
        //setup bot variables
        discCommandHandler = new DiscCommandHandler();
        discCommandParser = new DiscCommandParser();

        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(engine.getProperties().getDiscBotApplicationToken());
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        setBotApplicationGame(null, Game.GameType.DEFAULT);
        addBotApplicationCommands();
        addBotApplicationListeners();
        try {
            botJDA = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
            System.out.println("[Discord] !!!Bot start failure!!!\nMaybe the token invalid");
            botApplicationRunning = false;
            return;
        }
        System.out.println("[Discord] !Discord Bot successfully logged in!");
        if(engine.isViewLoaded())engine.getViewEngine().updateBotRunHomeButton();
    }

    private void setBotApplicationGame(String game, Game.GameType type) {
        builder.setGame(new Game("") {
            @Override
            public String getName() {
                if (game != null) {
                    return game;
                } else {
                    return engine.getProperties().getDiscBotApplicationGame();
                }
            }

            @Override
            public String getUrl() {
                return null;
            }

            @Override
            public Game.GameType getType() {
                return type;
            }
        });
    }

    private void addBotApplicationCommands() {
        System.out.println("[Discord] ~Add commands");
        discCommandHandler.createNewCommand("m", new DiscCMDMusic());
        discCommandHandler.createNewCommand("clear", new DiscCMDClear());
        discCommandHandler.createNewCommand("info", new DiscCMDInfo());
        discCommandHandler.createNewCommand("telegram", new DiscCMDTelegram());
    }

    private void addBotApplicationListeners() {
        System.out.println("[Discord] ~Add listeners");
        builder.addEventListener(new ServerMessageListener(engine));
        builder.addEventListener(new ServerJoinListener(engine));
    }

    public void rebootBotApplication() {
        if(!botApplicationRunning){
            System.out.println("[Discord] ~The bot is offline! Please start before restart");
            return;
        }
        shutdownBotApplication();
        startBotApplication();
    }

    public void shutdownBotApplication() {
        if(!botApplicationRunning){
            System.out.println("[Discord] ~The bot is already offline!");
            return;
        }
        System.out.println("[Discord] ~Bot shutting down!");
        try {
            botJDA.shutdownNow();
        } catch (Exception e) {
            System.out.println("[Discord] ~Bot cant shutdownBotApplication, eventually never starts?");
        }
        botApplicationRunning = false;
        engine.getViewEngine().updateBotRunHomeButton();
    }

    public DiscCommandHandler getDiscCommandHandler() {
        return discCommandHandler;
    }

    public DiscCommandParser getDiscCommandParser() {
        return discCommandParser;
    }

    public boolean isBotApplicationRunning() {
        return botApplicationRunning;
    }

    public boolean isDebugAi() {
        return debugAi;
    }

    public void setDebugAi(boolean debugAi) {
        this.debugAi = debugAi;
    }

    public DiscTextUtils getTextUtils() {
        return textUtils;
    }

    public DiscUtilityBase getUtilityBase() {
        return utilityBase;
    }

    public DiscApplicationFilesHandler getFilesHandler() {
        return filesHandler;
    }

    public JDA getBotJDA() {
        return botJDA;
    }
}