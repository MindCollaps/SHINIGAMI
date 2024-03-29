package botApplications.teleApplicationCore.utils;

import engines.Engine;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class TeleTextUtils {

    Engine engine;

    public TeleTextUtils(Engine engine) {
        this.engine = engine;
    }

    public void sendMessage(Long chatId, String message){
        SendResponse response = engine.getTeleApplicationEngine().getBot().execute(new SendMessage(chatId, message));
    }
}
