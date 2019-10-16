package botApplications.teleApplicationCore.teleApplicationCommands;

import engines.Engine;
import com.pengrad.telegrambot.model.Update;

public interface TeleCommand {

    boolean called(Update command, Engine engine, String[] args);
    void action(Update command, Engine engine, String[] args);
    String help(Engine engine, String[] args);
}
