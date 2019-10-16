package BotAiCore.librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class AiCmdModAnsReply implements Serializable  {

    public static final long serialVersionUID = 42L;
    ArrayList<String> humanReply = new ArrayList<>();
    ArrayList<String> replyAnswer = new ArrayList<>();

    public ArrayList<String> getHumanReply() {
        return humanReply;
    }

    public void setHumanReply(ArrayList<String> humanReply) {
        this.humanReply = humanReply;
    }

    public ArrayList<String> getReplyAnswer() {
        return replyAnswer;
    }

    public void setReplyAnswer(ArrayList<String> replyAnswer) {
        this.replyAnswer = replyAnswer;
    }
}
