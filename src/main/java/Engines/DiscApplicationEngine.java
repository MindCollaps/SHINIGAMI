package Engines;

import BotAiCore.Librarys.AiCmdModAnswer;
import BotAiCore.Librarys.AiCmdModification;
import BotAiCore.Librarys.AiCommand;
import BotApplications.DiscApplicationCore.DiscApplicationCommands.CMDClear;
import BotApplications.DiscApplicationCore.DiscApplicationCommands.CMDMusic;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationFilesHandler;
import BotApplications.DiscApplicationCore.DiscApplicationListeners.ServerMessageListener;
import BotApplications.DiscApplicationCore.DiscCommandHandler;
import BotApplications.DiscApplicationCore.DiscCommandParser;
import BotApplications.DiscApplicationCore.Utils.DiscTextUtils;
import BotApplications.DiscApplicationCore.Utils.DiscUtilityBase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

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
            System.out.println("!!!Bot is already running!!!");
            return;
        }
        //setup bot variables
        discCommandHandler = new DiscCommandHandler();
        discCommandParser = new DiscCommandParser(engine);

        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(engine.getProperties().getBotApplicationToken());
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        setBotApplicationGame(null, Game.GameType.DEFAULT);
        addBotApplicationCommands();
        addBotApplicationListeners();
        try {
            botJDA = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
            System.out.println("!!!Bot start failure!!!\nMaybe the token invalid");
            return;
        }
        System.out.println("!Bot successfully logged in!");
        botApplicationRunning = true;
        engine.getViewEngine().updateBotRunHomeButton();
    }

    private void setBotApplicationGame(String game, Game.GameType type) {
        builder.setGame(new Game("") {
            @Override
            public String getName() {
                if (game != null) {
                    return game;
                } else {
                    return engine.getProperties().getBotApplicationGame();
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
        System.out.println("~Add commands");
        discCommandHandler.createNewCommand("m", new CMDMusic());
        discCommandHandler.createNewCommand("clear", new CMDClear());
    }

    private void addBotApplicationListeners() {
        System.out.println("~Add listeners");
        builder.addEventListener(new ServerMessageListener(engine));
    }

    public void rebootBotApplication() {
        if(!botApplicationRunning){
            System.out.println("~The bot is offline! Please start before restart");
            return;
        }
        shutdownBotApplication();
        startBotApplication();
    }

    public void shutdownBotApplication() {
        if(!botApplicationRunning){
            System.out.println("~The bot is already offline!");
            return;
        }
        System.out.println("~Bot shutting down!");
        try {
            botJDA.shutdownNow();
        } catch (Exception e) {
            System.out.println("~Bot cant shutdownBotApplication, eventually never starts?");
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
}
