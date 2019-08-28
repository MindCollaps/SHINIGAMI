package BotAiCore.Librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class AiCommand implements Serializable {

    public static final long serialVersionUID = 42L;
    private ArrayList<AiCmdModification> modificators = new ArrayList<>();
    private ArrayList<String> humanSpellingList = new ArrayList<>();
    private String commandInvoke;

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
}
