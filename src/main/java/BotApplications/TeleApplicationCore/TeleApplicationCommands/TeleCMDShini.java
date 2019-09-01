package BotApplications.TeleApplicationCore.TeleApplicationCommands;

import Engines.Engine;
import com.pengrad.telegrambot.model.Update;

public class TeleCMDShini implements TeleCommand {

    @Override
    public boolean called(Update command, Engine engine, String[] args) {
        return true;
    }

    @Override
    public void action(Update command, Engine engine, String[] args) {
        if(command.message().text().length()<8){
            engine.getTeleApplicationEngine().getTextUtils().sendMessage(command.message().chat().id(),"Du musst schon irgentetwas dazu sagen, ich sag ja auch nich einfach \"" + command.message().authorSignature() + " was geht\" XD");
        }
        String msg = engine.getAiEngine().runAiForTelegram(command.message().text(), false, command.message().chat().username());
        if(msg!=null){
            engine.getTeleApplicationEngine().getTextUtils().sendMessage(command.message().chat().id(),msg);
        }
    }

    @Override
    public String help(Engine engine, String[] args) {
        return "With this command you can use teh shini chatbot";
    }
}
