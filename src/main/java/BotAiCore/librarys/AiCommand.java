package BotAiCore.librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class AiCommand implements Serializable {

    public static final long serialVersionUID = 42L;
    private ArrayList<AiCmdModification> modificators = new ArrayList<>();
    private ArrayList<String> humanSpellingList = new ArrayList<>();
    private String commandInvoke;
    private commandType commandType;

    public enum commandType{
        DISCORD {
            @Override
            public String toString() {
                return "discord";
            }
        },

        TELEGRAM{
            @Override
            public String toString() {
                return "telegram";
            }
        },

        ALL{
            @Override
            public String toString() {
                return "all";
            }
        }
    }


    public ArrayList<AiCmdModification> getModificators() {
        return modificators;
    }

    public void setModificators(ArrayList<AiCmdModification> modificators) {
        this.modificators = modificators;
    }

    public ArrayList<String> getHumanSpellingList() {
        return humanSpellingList;
    }

    public void setHumanSpellingList(ArrayList<String> humanSpellingList) {
        this.humanSpellingList = humanSpellingList;
    }

    public String getCommandInvoke() {
        return commandInvoke;
    }

    public void setCommandInvoke(String commandInvoke) {
        this.commandInvoke = commandInvoke;
    }

    public AiCommand.commandType getCommandType() {
        return commandType;
    }

    public void setCommandType(AiCommand.commandType commandType) {
        this.commandType = commandType;
    }
}