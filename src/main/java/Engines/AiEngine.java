package Engines;

import BotAiCore.Librarys.AiCmdModification;
import BotAiCore.Librarys.AiCommand;
import Engines.Engine;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class AiEngine {

    private ArrayList<AiCommand> aiCommands = new ArrayList<>();
    Engine engine;

    public AiEngine(Engine engine) {
        this.engine = engine;
    }

    public void runAi(GuildMessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        if (engine.getBotEngine().isDebugAi()) System.out.println("-----\n!!![Ai Engine] starts with operation\n");
        CommandSearchReturnValue command = new CommandSearchReturnValue(null,-1,false);
        ModificationSearchReturnValue modification = null;

        while (!command.completelyDone){
            try {
                command = analyzeTextForCommand(rawMessage, command.foundInt+1);
            } catch (Exception e) {
                if (engine.getBotEngine().isDebugAi())System.out.println("\n!!![Ai Engine] done with operation...result: command can't found \n-----");
                return;
            }
            if (engine.getBotEngine().isDebugAi())System.out.println("!---[Ai Engine] found matching command: " + command.command.getCommandInvoke() + "\n");
            try {
                modification = analyzeTextForCadModification(command.command, rawMessage);
                if (engine.getBotEngine().isDebugAi())System.out.println("!--[Ai Engine] found matching command modification for command: " + command.command.getCommandInvoke() + " mod: " + modification.modification.getInvoke() + "\n");
                break;
            } catch (Exception e) {
                if (engine.getBotEngine().isDebugAi())System.out.println("!--[Ai Engine] cant found matching modification for command: " + command.command.getCommandInvoke() + "\n");
            }
        }

        String behind = "";
        try {
            behind = getBehindCommand(rawMessage, command.foundInt, modification.foundInt);
        } catch (Exception e) {
            System.err.println("!!![Ai Engine] error in finding stuff behind command lol");
        }

        String commandToExecute = engine.getProperties().getBotApplicationPrefix() + command.command.getCommandInvoke() + " " + modification.modification.getInvoke() + " " + behind;

        runCommand(commandToExecute, event);
    }

    private String getBehindCommand(String text, int commandSearchResult, int modificationSearchResult) throws Exception{
        String[] messageArray = text.split(" ");
        String result;
        if(commandSearchResult>modificationSearchResult){
            result = messageArray[commandSearchResult+1];
            for (int i = commandSearchResult + 2; i < messageArray.length; i++) {
                result = result + " " + messageArray[i];
            }
        } else {
            result = messageArray[modificationSearchResult+1];
            for (int i = modificationSearchResult + 2; i < messageArray.length; i++) {
                result = result + " " + messageArray[i];
            }
        }
        return result;
    }

    private CommandSearchReturnValue analyzeTextForCommand(String analyzeText, int start) throws Exception {
        if (engine.getBotEngine().isDebugAi())
            System.out.println("!---[Ai Engine] Ai starts with analyzing: analyze text for command");
        AiCommand command;
        String[] messageToAnalyze = analyzeText.split(" ");
        int result = -100;
        for (int i = 0; i < messageToAnalyze.length; i++) {
            if (engine.getBotEngine().isDebugAi())
                System.out.println("---[Ai Engine] analyze message part: " + messageToAnalyze[i]);
            for (int j = start; j < aiCommands.size(); j++) {
                if (engine.getBotEngine().isDebugAi())
                    System.out.println("---[Ai Engine] compare message with command: " + aiCommands.get(j).getCommandInvoke());
                result = containsStartWith(aiCommands.get(j).getHumanSpellingList(), messageToAnalyze[i]);
                if (result == -1) {
                    if (engine.getBotEngine().isDebugAi())
                        System.out.println("---[Ai Engine] can not find match in command: " + aiCommands.get(j).getCommandInvoke() + "\n");
                    break;
                } else {
                    command = aiCommands.get(j);
                    if (aiCommands.size() == j) {
                        return new CommandSearchReturnValue(command, i, true);
                    } else {
                        return new CommandSearchReturnValue(command, i, false);
                    }
                }
            }
        }
        throw new Exception("[Ai Engine] cant found any matching command!");
    }

    private int containsStartWith(ArrayList<String> list, String stringCompare) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toLowerCase().startsWith(stringCompare.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    private ModificationSearchReturnValue analyzeTextForCadModification(AiCommand command, String messageToAnalyze) throws Exception {
        if (engine.getBotEngine().isDebugAi())
            System.out.println("!--[Ai Engine] Ai starts with analyzing: analyze text for command modification");
        String[] messageToAnalyzeArray = messageToAnalyze.split(" ");
        AiCmdModification modification;
        int result = -100;
        for (int i = 0; i < messageToAnalyzeArray.length; i++) {
            if (engine.getBotEngine().isDebugAi())
                System.out.println("--[Ai Engine] analyze message part: " + messageToAnalyzeArray[i]);
            for (int j = 0; j < command.getModificators().size(); j++) {
                modification = command.getModificators().get(j);
                result = containsStartWith(modification.getHumanSpellingList(), messageToAnalyzeArray[i]);
                if (engine.getBotEngine().isDebugAi())
                    System.out.println("--[Ai Engine] compare message with modification: " + modification.getInvoke());
                if(result==-1){
                    if (engine.getBotEngine().isDebugAi())
                        System.out.println("--[Ai Engine] can not find match in modification: " + modification.getInvoke() + "\n");
                } else {
                    return new ModificationSearchReturnValue(i, modification);
                }
            }
        }
        throw new Exception("[Ai Engine] cant found any matching modification!");
    }

    private void runCommand(String command, GuildMessageReceivedEvent event) {
        if (engine.getBotEngine().isDebugAi())
            System.out.println("\n!!![Ai Engine] done with operation...result: command send!\n-----");
        engine.getBotEngine().getUtilityBase().sendCommand(event, command);
    }

    public void addNewCommand(AiCommand command) {
        if(aiCommands.size()>0){
            if (aiCommands.contains(command)) {
                System.out.println("!!!Error command already exist!!!");
                return;
            }
        }
        aiCommands.add(command);
    }

    public void removeCommand(AiCommand command) {
        aiCommands.remove(command);
    }

    private class CommandSearchReturnValue {
        AiCommand command;
        int foundInt;
        boolean completelyDone;

        public CommandSearchReturnValue(AiCommand command, int foundInt, boolean completelyDone) {
            this.command = command;
            this.foundInt = foundInt;
            this.completelyDone = completelyDone;
        }
    }

    private class ModificationSearchReturnValue {
        int foundInt;
        AiCmdModification modification;

        public ModificationSearchReturnValue(int foundInt, AiCmdModification modification) {
            this.foundInt = foundInt;
            this.modification = modification;
        }
    }

    public void loadAiMind(){
        System.out.println("~load Ai mind");
        try {
            aiCommands = (ArrayList<AiCommand>) engine.getFileUtils().loadObject(engine.getFileUtils().getHome() + "/bot/mind/mind.exodus");
        } catch (Exception e) {
            System.out.println("!!!load Ai mind failed, maybe never created!");
        }
    }

    public void saveAiMind(){
        System.out.println("~save Ai mind");
        engine.getFileUtils().saveOject(engine.getFileUtils().getHome() + "/bot/mind/mind.exodus",aiCommands);
    }

    public ArrayList<AiCommand> getAiCommands() {
        return aiCommands;
    }

    public void setAiCommands(ArrayList<AiCommand> aiCommands) {
        this.aiCommands = aiCommands;
    }
}
