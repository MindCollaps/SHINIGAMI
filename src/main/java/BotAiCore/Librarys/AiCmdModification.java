package BotAiCore.Librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class AiCmdModification implements Serializable {

    public static final long serialVersionUID = 42L;
    private String invoke;
    private ArrayList<String> humanSpellingList = new ArrayList<>();
    private ArrayList<AiCmdModAnswer> answers = new ArrayList<>();

    public String getInvoke() {
        return invoke;
    }

    public void setInvoke(String invoke) {
        this.invoke = invoke;
    }

    public ArrayList<String> getHumanSpellingList() {
        return humanSpellingList;
    }

    public void setHumanSpellingList(ArrayList<String> humanSpellingList) {
        this.humanSpellingList = humanSpellingList;
    }

    public ArrayList<AiCmdModAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<AiCmdModAnswer> answers) {
        this.answers = answers;
    }
}
