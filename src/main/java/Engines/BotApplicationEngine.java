package Engines;

import BotAiCore.Librarys.AiCmdModAnswer;
import BotAiCore.Librarys.AiCmdModification;
import BotAiCore.Librarys.AiCommand;
import BotApplicationCore.BotApplicationCommands.CMDClear;
import BotApplicationCore.BotApplicationCommands.CMDMusic;
import BotApplicationCore.BotApplicationFiles.BotApplicationFilesHandler;
import BotApplicationCore.BotApplicationListeners.ServerMessageListener;
import BotApplicationCore.BotCommandHandler;
import BotApplicationCore.BotCommandParser;
import BotApplicationCore.Utils.BotTextUtils;
import BotApplicationCore.Utils.BotUtilityBase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class BotApplicationEngine {

    Engine engine;

    private JDABuilder builder;
    private JDA botJDA;

    private BotCommandHandler botCommandHandler;
    private BotCommandParser botCommandParser;

    private BotTextUtils textUtils;
    private BotUtilityBase utilityBase;
    private BotApplicationFilesHandler filesHandler;
    private AiEngine aiEngine;

    boolean botApplicationRunning = false;
    boolean debugAi = false;

    public BotApplicationEngine(Engine engine) {
        this.engine = engine;
        textUtils = new BotTextUtils(engine);
        utilityBase = new BotUtilityBase(engine);
        filesHandler = new BotApplicationFilesHandler(engine);
        aiEngine = new AiEngine(engine);
    }

    public void startBotApplication() {
        if (botApplicationRunning) {
            System.out.println("!!!Bot is already running!!!");
            return;
        }
        //setup bot variables
        botCommandHandler = new BotCommandHandler();
        botCommandParser = new BotCommandParser(engine);

        test();

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
        botCommandHandler.createNewCommand("m", new CMDMusic());
        botCommandHandler.createNewCommand("clear", new CMDClear());
    }

    private void addBotApplicationListeners() {
        System.out.println("~Add listeners");
        builder.addEventListener(new ServerMessageListener(engine));
    }

    public void reboot() {
        if(!botApplicationRunning){
            System.out.println("~The bot is offline! Please start before restart");
            return;
        }
        shutdown();
        startBotApplication();
    }

    public void shutdown() {
        if(!botApplicationRunning){
            System.out.println("~The bot is already offline!");
            return;
        }
        System.out.println("~Bot shutting down!");
        try {
            botJDA.shutdownNow();
        } catch (Exception e) {
            System.out.println("~Bot cant shutdown, eventually never starts?");
        }
        botApplicationRunning = false;
    }

    public void test (){
        AiCommand command = new AiCommand();
        AiCmdModification mod = new AiCmdModification();
        AiCmdModAnswer answer = new AiCmdModAnswer();

        ArrayList<AiCmdModAnswer> answers = new ArrayList<>();
        ArrayList<AiCmdModification> mods = new ArrayList<>();

        ArrayList<String> humanCommandSpelling = new ArrayList<>();
        ArrayList<String> humandModSpelling = new ArrayList<>();
        ArrayList<String> answerToSay = new ArrayList<>();

        command.setCommandInvoke("m");
        mod.setInvoke("play");

        humanCommandSpelling.add("lied");
        humandModSpelling.add("spiele");
        answer.setEmoteLevel("all");

        answers.add(answer);
        mods.add(mod);

        command.setHumanSpellingList(humanCommandSpelling);
        answer.setAnswers(answerToSay);
        mod.setAnswers(answers);
        mod.setHumanSpellingList(humandModSpelling);
        command.setModificators(mods);

        aiEngine.addNewCommand(command);
    }


    public BotCommandHandler getBotCommandHandler() {
        return botCommandHandler;
    }

    public BotCommandParser getBotCommandParser() {
        return botCommandParser;
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

    public BotTextUtils getTextUtils() {
        return textUtils;
    }

    public BotUtilityBase getUtilityBase() {
        return utilityBase;
    }

    public BotApplicationFilesHandler getFilesHandler() {
        return filesHandler;
    }

    public AiEngine getAiEngine() {
        return aiEngine;
    }
}
