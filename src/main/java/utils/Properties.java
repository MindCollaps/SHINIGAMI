package utils;

import java.io.Serializable;

public class Properties implements Serializable {

    public static final long serialVersionUID = 42L;

    //Generally stuff
    String botName = "SHINIGAMI";
    String botVersion = "1.2";
    String defaultYtPlaylist = "https://www.youtube.com/playlist?list=PLPvNmz-FXmoL10KOiP4sOrEzYlU2kgZ4r";

    //Discord BotApplication stuff
    String discBotApplicationToken = "";
    String discBotApplicationGame = "Daddelt am Handy";
    String discBotApplicationPrefix = "-";

    //Discord BotCommands stuff
    String commandInvokePreverences = "properties";
    String commandInvokeMusic = "m";

    //telegram BotApplication stuff
    String teleBotApplicationToken = "";

    //BotMessage times
    private int veryLongTime = 60000;
    private int longTime = 10000;
    private int middleTime = 6000;
    private int shortTime = 4000;

    //Engine stuff
    boolean debug = false;

    public int getPlanedProgrammShutdownOnUserAction() {
        //Shutdown codes
        int planedProgrammShutdownOnUserAction = 1;
        return planedProgrammShutdownOnUserAction;
    }

    public int getPlanedProgrammShutdownOnAllertboxAction() {
        int planedProgrammShutdownOnAllertboxAction = 20;
        return planedProgrammShutdownOnAllertboxAction;
    }

    public int getPlannedProgrammShutdownOnError() {
        //shutdownBotApplication error codes
        int plannedProgrammShutdownOnError = -5;
        return plannedProgrammShutdownOnError;
    }

    public String getDiscBotApplicationToken() {
        return discBotApplicationToken;
    }

    public void setDiscBotApplicationToken(String discBotApplicationToken) {
        this.discBotApplicationToken = discBotApplicationToken;
    }

    public String getDiscBotApplicationGame() {
        return discBotApplicationGame;
    }

    public void setDiscBotApplicationGame(String discBotApplicationGame) {
        this.discBotApplicationGame = discBotApplicationGame;
    }

    public String getBotVersion() {
        return botVersion;
    }

    public void setBotVersion(String botVersion) {
        this.botVersion = botVersion;
    }

    public String getDiscBotApplicationPrefix() {
        return discBotApplicationPrefix;
    }

    public void setDiscBotApplicationPrefix(String discBotApplicationPrefix) {
        this.discBotApplicationPrefix = discBotApplicationPrefix;
    }

    public String getCommandInvokePreverences() {
        return commandInvokePreverences;
    }

    public void setCommandInvokePreverences(String commandInvokePreverences) {
        this.commandInvokePreverences = commandInvokePreverences;
    }

    public String getCommandInvokeMusic() {
        return commandInvokeMusic;
    }

    public void setCommandInvokeMusic(String commandInvokeMusic) {
        this.commandInvokeMusic = commandInvokeMusic;
    }

    public String getDefaultYtPlaylist() {
        return defaultYtPlaylist;
    }

    public void setDefaultYtPlaylist(String defaultYtPlaylist) {
        this.defaultYtPlaylist = defaultYtPlaylist;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getVeryLongTime() {
        return veryLongTime;
    }

    public void setVeryLongTime(int veryLongTime) {
        this.veryLongTime = veryLongTime;
    }

    public int getLongTime() {
        return longTime;
    }

    public void setLongTime(int longTime) {
        this.longTime = longTime;
    }

    public int getMiddleTime() {
        return middleTime;
    }

    public void setMiddleTime(int middleTime) {
        this.middleTime = middleTime;
    }

    public int getShortTime() {
        return shortTime;
    }

    public void setShortTime(int shortTime) {
        this.shortTime = shortTime;
    }

    public String getTeleBotApplicationToken() {
        return teleBotApplicationToken;
    }

    public void setTeleBotApplicationToken(String teleBotApplicationToken) {
        this.teleBotApplicationToken = teleBotApplicationToken;
    }
}
