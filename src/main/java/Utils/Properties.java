package Utils;

import java.io.Serializable;

public class Properties implements Serializable {

    public static final long serialVersionUID = 42L;

    //Discord BotApplication stuff
    String discBotApplicationToken = "";
    String discBotApplicationName = "SHINIGAMI";
    String discBotApplicationGame = "Daddelt am Handy";
    String discBotApplicationVersion = "1.2";
    String discBotApplicationPrefix = "-";

    //Discord BotApplication nice to have
    String commandMusicDefaultYTPlaylist = "https://www.youtube.com/playlist?list=PLPvNmz-FXmoL10KOiP4sOrEzYlU2kgZ4r";

    //Discrod BotCommands stuff
    String commandInvokePreverences = "properties";
    String commandInvokeMusic = "m";

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

    public String getDiscBotApplicationVersion() {
        return discBotApplicationVersion;
    }

    public void setDiscBotApplicationVersion(String discBotApplicationVersion) {
        this.discBotApplicationVersion = discBotApplicationVersion;
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

    public String getCommandMusicDefaultYTPlaylist() {
        return commandMusicDefaultYTPlaylist;
    }

    public void setCommandMusicDefaultYTPlaylist(String commandMusicDefaultYTPlaylist) {
        this.commandMusicDefaultYTPlaylist = commandMusicDefaultYTPlaylist;
    }

    public String getDiscBotApplicationName() {
        return discBotApplicationName;
    }

    public void setDiscBotApplicationName(String discBotApplicationName) {
        this.discBotApplicationName = discBotApplicationName;
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


}
