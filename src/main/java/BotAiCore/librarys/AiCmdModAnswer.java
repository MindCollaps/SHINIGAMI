package BotAiCore.librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class AiCmdModAnswer implements Serializable {

    public static final long serialVersionUID = 42L;
    private ArrayList<String> answers = new ArrayList<>();
    private String emoteLevel;
    private ArrayList<AiCmdModAnsReply> replies = new ArrayList<>();

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

    public ArrayList<AiCmdModAnsReply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<AiCmdModAnsReply> replies) {
        this.replies = replies;
    }
}
