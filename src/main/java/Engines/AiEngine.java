package Engines;

import BotAiCore.Librarys.AiCmdModAnswer;
import BotAiCore.Librarys.AiCmdModification;
import BotAiCore.Librarys.AiCommand;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationServer;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class AiEngine {

    private ArrayList<AiCommand> aiCommands = new ArrayList<>();
    Engine engine;
    boolean exCommand;

    public AiEngine(Engine engine) {
        this.engine = engine;
    }

    //per discord
    public void runAi(GuildMessageReceivedEvent event) {
        AnalyzeTextReturnValue returnValue;
        try {
            returnValue = analyzeText(event.getMessage().getContentRaw(), AiCommand.commandType.DISCORD);
        } catch (Exception e) {
            return;
        }

        DiscApplicationServer server = null;
        DiscApplicationUser user = null;

        if (exCommand) {
            try {
                server = engine.getDiscEngine().getFilesHandler().getServerById(event.getGuild().getId());
            } catch (Exception e) {
                engine.getUtilityBase().printDebug("![Ai Engine] " + event.getGuild().getId() + " Server not found!");
            }
        }

        try {
            user = engine.getDiscEngine().getFilesHandler().getUserById(event.getAuthor().getId());
        } catch (Exception e) {
            engine.getUtilityBase().printDebug("![Ai Engine] " + event.getAuthor().getId() + " User not found!");
        }

        String commandToExecute = engine.getProperties().getBotApplicationPrefix() + returnValue.command.getCommandInvoke() + " " + returnValue.modification.getInvoke() + " " + returnValue.behind;

        try {
            talk(returnValue.modification.getAnswers(), event, user, returnValue.behind);
        } catch (Exception e) {
        }

        if (exCommand) runCommand(commandToExecute, event, user, server);
    }

    public String runAi(String message, boolean debugmode) {
        AnalyzeTextReturnValue returnValue;
        try {
            returnValue = analyzeText(message, AiCommand.commandType.ALL);
        } catch (Exception e) {
            return null;
        }
        String stringReturnValue = "";
        if (debugmode){
            try {
                stringReturnValue = talkDebugMode(returnValue.modification.getAnswers(), returnValue.behind, 0);
            } catch (Exception e) {
            }
        } else {
            try {
                stringReturnValue = talk(returnValue.modification.getAnswers(), returnValue.behind, 0);
            } catch (Exception e) {
            }
        }
        return stringReturnValue;
    }

    private AnalyzeTextReturnValue analyzeText(String rawMessage, AiCommand.commandType commandType) throws Exception {
        exCommand = true;
        if (engine.getDiscEngine().isDebugAi()) System.out.println("-----\n!!![Ai Engine] starts with operation\n");
        CommandSearchReturnValue commandSearchReturnValue = new CommandSearchReturnValue();
        ModificationSearchReturnValue modificationSearchReturnValue = null;

        while (!commandSearchReturnValue.completelyDone) {
            try {
                commandSearchReturnValue = analyzeTextForCommand(rawMessage, commandType);
            } catch (Exception e) {
                if (engine.getDiscEngine().isDebugAi())
                    System.out.println("\n!!![Ai Engine] done with operation...result: command cant found!\n----!");
                throw new Exception("command cant found!");
            }
            if (engine.getDiscEngine().isDebugAi())
                System.out.println("!---[Ai Engine] found matching command: " + commandSearchReturnValue.command.getCommandInvoke() + "\n");
            try {
                modificationSearchReturnValue = analyzeTextForCadModification(commandSearchReturnValue.command, rawMessage);
                if (engine.getDiscEngine().isDebugAi())
                    System.out.println("!--[Ai Engine] found matching command modification for command: " + commandSearchReturnValue.command.getCommandInvoke() + " mod: " + modificationSearchReturnValue.modification.getInvoke() + "\n");
                break;
            } catch (Exception e) {
                if (engine.getDiscEngine().isDebugAi())
                    System.out.println("!--[Ai Engine] cant found matching modification for command: " + commandSearchReturnValue.command.getCommandInvoke() + "\n");
            }
            if (commandSearchReturnValue.completelyDone) {
                if (engine.getDiscEngine().isDebugAi())
                    System.out.println("\n!!![Ai Engine] done with operation...result: command cant found!\n----!");
                throw new Exception("command cant found!");
            }
        }

        String behind = "";
        try {
            behind = getBehindCommand(rawMessage, commandSearchReturnValue.foundInt, modificationSearchReturnValue.foundInt);
        } catch (Exception e) {
            System.err.println("!!![Ai Engine] error in finding stuff behind command lol");
        }

        if (commandSearchReturnValue.command.getCommandInvoke().toLowerCase().startsWith("talk")) exCommand = false;

        return new AnalyzeTextReturnValue(commandSearchReturnValue.command, modificationSearchReturnValue.modification, behind);
    }

    private String getBehindCommand(String text, int commandSearchResult, int modificationSearchResult) throws Exception {
        String[] messageArray = text.split(" ");
        String result;
        if (commandSearchResult > modificationSearchResult) {
            result = messageArray[commandSearchResult + 1];
            for (int i = commandSearchResult + 2; i < messageArray.length; i++) {
                result = result + " " + messageArray[i];
            }
        } else {
            result = messageArray[modificationSearchResult + 1];
            for (int i = modificationSearchResult + 2; i < messageArray.length; i++) {
                result = result + " " + messageArray[i];
            }
        }
        return result;
    }

    private CommandSearchReturnValue analyzeTextForCommand(String analyzeText, AiCommand.commandType commandType) throws Exception {
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("!---[Ai Engine] Ai starts with analyzing: analyze text for command\n");
        AiCommand command;
        String[] messageToAnalyze = analyzeText.split(" ");
        int result = -100;
        for (int i = 0; i < messageToAnalyze.length; i++) {
            if (engine.getDiscEngine().isDebugAi())
                System.out.println("---[Ai Engine] -- analyze message part: " + messageToAnalyze[i] + " --");
            for (int j = 0; j < aiCommands.size(); j++) {
                command = aiCommands.get(j);
                if (!command.getCommandType().equals(commandType)){
                    if(!command.getCommandType().equals(AiCommand.commandType.ALL)){
                        continue;
                    }
                }
                if (engine.getDiscEngine().isDebugAi())
                    System.out.println("---[Ai Engine] compare message with command: " + aiCommands.get(j).getCommandInvoke());
                result = containsStartWith(command.getHumanSpellingList(), messageToAnalyze[i]);
                if (result == -1) {
                    if (engine.getDiscEngine().isDebugAi())
                        System.out.println("---[Ai Engine] can not find match in command: " + aiCommands.get(j).getCommandInvoke() + "\n");
                } else {
                    if (aiCommands.size() == j + 1) {
                        return new CommandSearchReturnValue().init(command, i, true);
                    } else {
                        return new CommandSearchReturnValue().init(command, i, false);
                    }
                }
            }
        }
        throw new Exception("[Ai Engine] cant found any matching command!");
    }

    private int containsStartWith(ArrayList<String> list, String stringCompare) {
        for (int i = 0; i < list.size(); i++) {
            if (stringCompare.toLowerCase().contains(list.get(i).toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    private ModificationSearchReturnValue analyzeTextForCadModification(AiCommand command, String messageToAnalyze) throws Exception {
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("!--[Ai Engine] Ai starts with analyzing: analyze text for command modification\n");
        String[] messageToAnalyzeArray = messageToAnalyze.split(" ");
        AiCmdModification modification;
        int result = -100;
        for (int i = 0; i < messageToAnalyzeArray.length; i++) {
            if (engine.getDiscEngine().isDebugAi())
                System.out.println("--[Ai Engine] -- analyze message part: " + messageToAnalyzeArray[i] + " --");
            for (int j = 0; j < command.getModificators().size(); j++) {
                modification = command.getModificators().get(j);
                result = containsStartWith(modification.getHumanSpellingList(), messageToAnalyzeArray[i]);
                if (engine.getDiscEngine().isDebugAi())
                    System.out.println("--[Ai Engine] compare message with modification: " + modification.getInvoke());
                if (result == -1) {
                    if (engine.getDiscEngine().isDebugAi())
                        System.out.println("--[Ai Engine] can not find match in modification: " + modification.getInvoke() + "\n");
                } else {
                    return new ModificationSearchReturnValue(i, modification);
                }
            }
        }
        throw new Exception("[Ai Engine] cant found any matching modification!");
    }

    private void talk(ArrayList<AiCmdModAnswer> answers, GuildMessageReceivedEvent event, DiscApplicationUser user, String behind) throws Exception {
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("-![Ai Engine] start talking!");
        AiCmdModAnswer current;
        ArrayList emotes;
        for (int i = 0; i < answers.size(); i++) {
            current = answers.get(i);
            emotes = new ArrayList<>();
            emotes.addAll(Arrays.asList(current.getEmoteLevel().split(",")));
            if (emotes.contains(user.getEmoteLevel()) || current.getEmoteLevel().equalsIgnoreCase("all")) {
                String randomMessage = current.getAnswers().get(ThreadLocalRandom.current().nextInt(0, current.getAnswers().size()));
                event.getChannel().sendMessage(buildDiscordMessage(randomMessage, user, behind).build()).complete();
                return;
            }
        }
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("!!![Ai Engine] Ai had error in talk segment!");
        throw new Exception("no valid emote level found!");
    }

    private String talk(ArrayList<AiCmdModAnswer> answers, String behind, int emotelv) throws Exception {
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("-![Ai Engine] start talking!");

        AiCmdModAnswer current;
        ArrayList emotes;
        for (int i = 0; i < answers.size(); i++) {
            current = answers.get(i);
            emotes = new ArrayList<>();
            emotes.addAll(Arrays.asList(current.getEmoteLevel().split(",")));
            if (emotes.contains(emotelv) || current.getEmoteLevel().equalsIgnoreCase("all")) {
                String randomMessage = current.getAnswers().get(ThreadLocalRandom.current().nextInt(0, current.getAnswers().size()));
                return buildNormalMessage(randomMessage, behind);
            }
        }
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("!!![Ai Engine] Ai had error in talk segment!");
        throw new Exception("no valid emote level found!");
    }

    private String talkDebugMode(ArrayList<AiCmdModAnswer> answers, String behind, int emotelv) throws Exception {
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("-![Ai Engine] start talking!");

        AiCmdModAnswer current;
        ArrayList emotes;
        for (int i = 0; i < answers.size(); i++) {
            current = answers.get(i);
            emotes = new ArrayList<>();
            emotes.addAll(Arrays.asList(current.getEmoteLevel().split(",")));
            if (emotes.contains(emotelv) || current.getEmoteLevel().equalsIgnoreCase("all")) {
                String randomMessage = current.getAnswers().get(ThreadLocalRandom.current().nextInt(0, current.getAnswers().size()));
                return randomMessage;
            }
        }
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("!!![Ai Engine] Ai had error in talk segment!");
        throw new Exception("no valid emote level found!");
    }

    private String buildNormalMessage(String answer, String behind) {
        String[] msgParts = answer.split(" ");
        ArrayList skip = new ArrayList();
        for (int i = 0; i < msgParts.length; i++) {
            if (msgParts[i].startsWith("*color.") && msgParts[i].endsWith("*")) {
                skip.add(String.valueOf(i));
                break;
            } else if (msgParts[i].startsWith("*title.") && msgParts[i].endsWith("*")) {
                skip.add(String.valueOf(i));
                break;
            } else if (msgParts[i].startsWith("*image.") && msgParts[i].endsWith("*")) {
                skip.add(String.valueOf(i));
                break;
            } else if (msgParts[i].startsWith("*ignore*")) {
                skip.add(String.valueOf(i));
                exCommand = false;
            } else if (msgParts[i].startsWith("*rise.") && msgParts[i].endsWith("*")) {
                skip.add(String.valueOf(i));
                break;
            } else if (msgParts[i].startsWith("*increase.") && msgParts[i].endsWith("*")) {
                skip.add(String.valueOf(i));
                break;
            }
        }
        String msgToSend = "";
        for (int i = 0; i < msgParts.length; i++) {
            boolean dell = false;
            for (int j = 0; j < skip.size(); j++) {
                if (i == Integer.valueOf((String) skip.get(j))) {
                    dell = true;
                }
            }
            if (msgParts[i].toLowerCase().startsWith("*n*")) {
                dell = true;
                msgToSend = msgToSend + "\n";
            }
            if (msgParts[i].toLowerCase().startsWith("*user*")) {
                dell = true;
            }
            if (msgParts[i].toLowerCase().startsWith("*modifier*")) {
                dell = true;
                msgToSend = msgToSend + " " + behind;
            }

            if (!dell) {
                msgToSend = msgToSend + " " + msgParts[i];
            }
        }
        return msgToSend;
    }

    private EmbedBuilder buildDiscordMessage(String answer, DiscApplicationUser user, String behind) {
        String[] msgParts = answer.split(" ");
        EmbedBuilder builder = new EmbedBuilder();
        ArrayList skip = new ArrayList();
        for (int i = 0; i < msgParts.length; i++) {
            if (msgParts[i].startsWith("*color.") && msgParts[i].endsWith("*")) {
                skip.add(String.valueOf(i));
                try {
                    builder.setColor(engine.getUtilityBase().convertStringToColor(msgParts[i].substring(7, msgParts[i].length() - 1)));
                } catch (Exception e) {
                    if (engine.getDiscEngine().isDebugAi()) {
                        System.out.println("Color error");
                    }
                }
                break;
            } else if (msgParts[i].startsWith("*title.") && msgParts[i].endsWith("*")) {
                skip.add(String.valueOf(i));
                try {
                    builder.setTitle(msgParts[i].substring(6, msgParts[i].length() - 1).replace("_", " "));
                } catch (Exception e) {
                    if (engine.getDiscEngine().isDebugAi()) {
                        System.out.println("Title error");
                    }
                }
                break;
            } else if (msgParts[i].startsWith("*image.") && msgParts[i].endsWith("*")) {
                skip.add(String.valueOf(i));
                try {
                    builder.setImage(msgParts[i].substring(6, msgParts[i].length() - 1));
                } catch (Exception e) {
                    if (engine.getDiscEngine().isDebugAi()) {
                        System.out.println("image error");
                    }
                }
                break;
            } else if (msgParts[i].startsWith("*ignore*")) {
                skip.add(String.valueOf(i));
                exCommand = false;
                break;
            } else if (msgParts[i].startsWith("*rise.") && msgParts[i].endsWith("*")) {
                user.riseEmoteLevel(Integer.valueOf(msgParts[i].substring(5, msgParts[i].length() - 1)));
                break;
            } else if (msgParts[i].startsWith("*increase.") && msgParts[i].endsWith("*")) {
                user.inceaseEmoteLevel(Integer.valueOf(msgParts[i].substring(9, msgParts[i].length() - 1)));
                break;
            }
        }
        String msgToSend = "";
        for (int i = 0; i < msgParts.length; i++) {
            boolean dell = false;
            for (int j = 0; j < skip.size(); j++) {
                if (i == Integer.valueOf((String) skip.get(j))) {
                    dell = true;
                }
            }
            if (msgParts[i].toLowerCase().startsWith("*n*")) {
                dell = true;
                msgToSend = msgToSend + "\n";
            }
            if (msgParts[i].toLowerCase().startsWith("*user*")) {
                dell = true;
                msgToSend = msgToSend + " " + user.getUserName();
            }
            if (msgParts[i].toLowerCase().startsWith("*modifier*")) {
                dell = true;
                msgToSend = msgToSend + " " + behind;
            }
            if (!dell) {
                msgToSend = msgToSend + " " + msgParts[i];
            }
        }
        builder.setDescription(msgToSend);
        return builder;
    }

    public void analyzeMessageForEmotes(String[] message, DiscApplicationUser user, String[] badEmo, String[] luckyEmo, String[] loveEmo) {
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("-![Ai Engine] scan for emotion!");

        int hasBadEmo = 0;
        int hasLuckyEmo = 0;
        int hasLoveEmo = 0;

        for (int i = 0; i < message.length; i++) {
            for (int b = 0; b < badEmo.length; b++) {
                if (message[i].toLowerCase().contains(badEmo[b].toLowerCase())) {
                    hasBadEmo++;
                    if (engine.getDiscEngine().isDebugAi())
                        System.out.println("-![Ai Engine] found bad emotion!");
                }
            }
            for (int b = 0; b < luckyEmo.length; b++) {
                if (message[i].toLowerCase().contains(luckyEmo[b].toLowerCase())) {
                    hasLuckyEmo++;
                    if (engine.getDiscEngine().isDebugAi())
                        System.out.println("-![Ai Engine] found lucky emotion!");
                }
            }
            for (int b = 0; b < loveEmo.length; b++) {
                if (message[i].toLowerCase().contains(loveEmo[b].toLowerCase())) {
                    hasLoveEmo++;
                    if (engine.getDiscEngine().isDebugAi())
                        System.out.println("-![Ai Engine] found love emotion!");
                }
            }
        }
        if (hasBadEmo > 0) {
            for (int i = 0; i < hasBadEmo; i++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 10);
                if (randomNum > 4) {
                    if (engine.getDiscEngine().isDebugAi())
                        System.out.println("-![Ai Engine] increase emote level from user!");
                    user.inceaseEmoteLevel(1);
                }
            }
        }
        if (hasLoveEmo > 0) {
            for (int i = 0; i < hasLoveEmo; i++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 10);
                if (randomNum > 4) {
                    user.riseEmoteLevel(2);
                    if (engine.getDiscEngine().isDebugAi())
                        System.out.println("-![Ai Engine] rise(2) emote from user!");
                }
            }
        }
        if (hasLuckyEmo > 0) {
            for (int i = 0; i < hasLuckyEmo; i++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 10);
                if (randomNum > 4) {
                    user.riseEmoteLevel(1);
                    if (engine.getDiscEngine().isDebugAi())
                        System.out.println("-![Ai Engine] rise(1) emote from user!");
                }
            }
        }
    }

    private void runCommand(String command, GuildMessageReceivedEvent event, DiscApplicationUser user, DiscApplicationServer server) {
        if (engine.getDiscEngine().isDebugAi())
            System.out.println("\n!!![Ai Engine] done with operation...result: command send!\n-----");
        engine.getDiscEngine().getUtilityBase().sendOwnCommand(event, command, user, server);
    }

    public void addNewCommand(AiCommand command) {
        if (aiCommands.size() > 0) {
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
        boolean completelyDone = false;

        public CommandSearchReturnValue init(AiCommand command, int foundInt, boolean completelyDone) {
            this.command = command;
            this.foundInt = foundInt;
            this.completelyDone = completelyDone;
            return this;
        }
    }

    private class AnalyzeTextReturnValue {
        AiCommand command;
        AiCmdModification modification;
        String behind;

        public AnalyzeTextReturnValue(AiCommand command, AiCmdModification modification, String behind) {
            this.command = command;
            this.modification = modification;
            this.behind = behind;
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

    public void loadAiMind() {
        System.out.println("~load Ai mind");
        try {
            aiCommands = (ArrayList<AiCommand>) engine.getFileUtils().loadObject(engine.getFileUtils().getHome() + "/bot/mind/mind.exodus");
        } catch (Exception e) {
            System.out.println("!!!load Ai mind failed, maybe never created!");
        }
    }

    public void saveAiMind() {
        System.out.println("~save Ai mind");
        engine.getFileUtils().saveOject(engine.getFileUtils().getHome() + "/bot/mind/mind.exodus", aiCommands);
    }

    public ArrayList<AiCommand> getAiCommands() {
        return aiCommands;
    }

    public void setAiCommands(ArrayList<AiCommand> aiCommands) {
        this.aiCommands = aiCommands;
    }
}
