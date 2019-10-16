package botApplications.teleApplicationCore.utils;

import engines.Engine;
import com.pengrad.telegrambot.model.Update;

public class TeleBotUtils {

    Engine engine;

    public TeleBotUtils(Engine engine) {
        this.engine = engine;
    }

    public void sendCommand(Update update){
        engine.getTeleApplicationEngine().getCommandHandler().handleCommand(engine.getTeleApplicationEngine().getCommandParser().parseCommand(update.message().text(),engine, update));
    }

    public void sendOwnCommand(Update update, String own){
        engine.getTeleApplicationEngine().getCommandHandler().handleCommand(engine.getTeleApplicationEngine().getCommandParser().parseCommand(own,engine, update));
    }
}
