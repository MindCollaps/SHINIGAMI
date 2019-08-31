package BotApplications.TeleApplicationCore.Utils;

import Engines.Engine;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class TeleBotUtils {

    Engine engine;

    public TeleBotUtils(Engine engine) {
        this.engine = engine;
    }

    public void sendMessage(Long chatId, String message){
        SendResponse response = engine.getTeleApplicationEngine().getBot().execute(new SendMessage(chatId, message));
    }
}
