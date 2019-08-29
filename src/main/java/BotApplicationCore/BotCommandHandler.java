package BotApplicationCore;

import BotApplicationCore.BotApplicationCommands.Command;
import Engines.Engine;

import java.util.ArrayList;
import java.util.HashMap;

public class BotCommandHandler {

    public BotCommandParser parser;
    public HashMap<String, Command> commands = new HashMap<>();
    public ArrayList<String> commandIvokes = new ArrayList<>();

    public void handleServerCommand(BotCommandParser.serverCommandContainer cmd) {

        if (commands.containsKey(cmd.invoke)) {

            boolean exe = commands.get(cmd.invoke).calledServer(cmd.args, cmd.event, cmd.server, cmd.user, cmd.engine);

            if (exe) {
                String args0 = "";
                try {
                    args0 = cmd.args[0];
                } catch (Exception ignored) {
                }
                if (args0.equalsIgnoreCase("help")) {
                    cmd.engine.getBotEngine().getTextUtils().sendHelp(commands.get(cmd.invoke).help(cmd.engine), cmd.event.getChannel());
                } else {
                    commands.get(cmd.invoke).actionServer(cmd.args, cmd.event, cmd.server, cmd.user, cmd.engine);
                }
            }
        }
    }

    public void handlePrivateCommand(BotCommandParser.clientCommandContainer cmd) {

        if (commands.containsKey(cmd.invoke)) {

            boolean exe = commands.get(cmd.invoke).calledPrivate(cmd.args, cmd.event, cmd.user, cmd.engine);

            if (exe) {
                String args0 = "";
                try {
                    args0 = cmd.args[0];
                } catch (Exception ignored) {
                }

                if (args0.equalsIgnoreCase("help")) {
                    cmd.engine.getBotEngine().getTextUtils().sendHelp(commands.get(cmd.invoke).help(cmd.engine), cmd.event.getChannel());
                } else {
                    commands.get(cmd.invoke).actionPrivate(cmd.args, cmd.event, cmd.user, cmd.engine);
                }
            }
        }
    }

    public void createNewCommand(String ivoke, Command cmd) {
        commands.put(ivoke, cmd);
        commandIvokes.add(ivoke);
    }
}
