package BotAiCore.Librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class AiCmdModAnswer implements Serializable {

    public static final long serialVersionUID = 42L;
    private ArrayList<String> answers = new ArrayList<>();
    private String emoteLevel;

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getEmoteLevel() {
        return emoteLevel;
    }

    public void setEmoteLevel(String emoteLevel) {
        this.emoteLevel = emoteLevel;
    }
}
