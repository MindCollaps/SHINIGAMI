package Utils;

import java.io.Serializable;

public class Properties implements Serializable {

    public static final long serialVersionUID = 42L;

    //BotApplication stuff
    String botApplicationToken = "";
    String botApplicationName = "SHINIGAMI";
    String botApplicationGame = "Daddelt am Handy";
    String botApplicationVersion = "1.2";
    String botApplicationPrefix = "-";

    //BotApplication nice to have
    String commandMusicDefaultYTPlaylist = "https://www.youtube.com/playlist?list=PLPvNmz-FXmoL10KOiP4sOrEzYlU2kgZ4r";

    //BotCommands stuff
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
        //shutdown error codes
        int plannedProgrammShutdownOnError = -5;
        return plannedProgrammShutdownOnError;
    }

    public String getBotApplicationToken() {
        return botApplicationToken;
    }

    public void setBotApplicationToken(String botApplicationToken) {
        this.botApplicationToken = botApplicationToken;
    }

    public String getBotApplicationGame() {
        return botApplicationGame;
    }

    public void setBotApplicationGame(String botApplicationGame) {
        this.botApplicationGame = botApplicationGame;
    }

    public String getBotApplicationVersion() {
        return botApplicationVersion;
    }

    public void setBotApplicationVersion(String botApplicationVersion) {
        this.botApplicationVersion = botApplicationVersion;
    }

    public String getBotApplicationPrefix() {
        return botApplicationPrefix;
    }

    public void setBotApplicationPrefix(String botApplicationPrefix) {
        this.botApplicationPrefix = botApplicationPrefix;
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

    public String getBotApplicationName() {
        return botApplicationName;
    }

    public void setBotApplicationName(String botApplicationName) {
        this.botApplicationName = botApplicationName;
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
