package BotApplications.TeleApplicationCore.TeleApplicationListeners;

import Engines.Engine;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import javafx.application.Platform;

import java.util.List;

public class MessageListener implements UpdatesListener {

    Engine engine;

    public MessageListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public int process(List<Update> list) {
        Update current = list.get(0);
        Platform.runLater(new CommandDispatcherThread(current));
        return list.get(0).updateId();
    }

    private void sendCommand(Update update){
        engine.getTeleApplicationEngine().getBotUtils().sendCommand(update);
    }

    private class CommandDispatcherThread implements Runnable{

        Update update;

        public CommandDispatcherThread(Update update) {
            this.update = update;
        }

        @Override
        public void run() {
            if(update.message().text()!=null){
                if(update.message().text().toLowerCase().startsWith("/")){
                    if(update.message().text().length()<8){
                        engine.getTeleApplicationEngine().getTextUtils().sendMessage(update.message().chat().id(), "Du musst schon irgendwas sagen haha XD");
                    } else {
                        sendCommand(update);
                    }
                }
            }
        }
    }
}
