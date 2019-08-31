package BotApplications.TeleApplicationCore.TeleApplicationCommands;

import Engines.Engine;
import com.pengrad.telegrambot.model.Update;

public interface TeleCommand {

    boolean called(Update command, Engine engine);
    void action(Update command, Engine engine);
    String help(Update command, Engine engine);
}
