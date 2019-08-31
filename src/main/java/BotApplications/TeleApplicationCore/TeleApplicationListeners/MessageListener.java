package BotApplications.TeleApplicationCore.TeleApplicationListeners;

import Engines.Engine;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

public class MessageListener implements UpdatesListener {

    Engine engine;

    public MessageListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public int process(List<Update> list) {
        Update current = list.get(0);

        if(current.message().text()!=null){
            if(current.message().text().toLowerCase().startsWith("/shini")){
                if(current.message().text().length()<8){
                    engine.getTeleApplicationEngine().getBotUtils().sendMessage(current.message().chat().id(), "Du musst schon irgendwas sagen haha XD");
                    return list.get(0).updateId();
                }
                String msg = engine.getAiEngine().runAi(current.message().text(), false);
                if(msg!=null){
                    engine.getTeleApplicationEngine().getBotUtils().sendMessage(current.message().chat().id(),msg);
                }
            }

            if(current.message().text().toLowerCase().startsWith("/")){
                sendCommand(current);
            }
        }

        return list.get(0).updateId();
    }

    private void sendCommand(Update update){
        if(update.message().text().equalsIgnoreCase("/start")){
            engine.getTeleApplicationEngine().getBotUtils().sendMessage(update.message().chat().id(), "DAAAAAAASSSSSS is voll unnötig lool XD aba benutz doch ma /shini :D");
        }
    }
}
