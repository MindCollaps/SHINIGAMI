package BotApplications.DiscApplicationCore;

import BotApplications.DiscApplicationCore.DiscApplicationCommands.Command;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscCommandHandler {

    public DiscCommandParser parser;
    public HashMap<String, Command> commands = new HashMap<>();
    public ArrayList<String> commandIvokes = new ArrayList<>();

    public void handleServerCommand(DiscCommandParser.serverCommandContainer cmd) {

        if (commands.containsKey(cmd.invoke)) {

            boolean exe = commands.get(cmd.invoke).calledServer(cmd.args, cmd.event, cmd.server, cmd.user, cmd.engine);

            if (exe) {
                String args0 = "";
                try {
                    args0 = cmd.args[0];
                } catch (Exception ignored) {
                }
                if (args0.equalsIgnoreCase("help")) {
                    cmd.engine.getDiscEngine().getTextUtils().sendHelp(commands.get(cmd.invoke).help(cmd.engine), cmd.event.getChannel());
                } else {
                    commands.get(cmd.invoke).actionServer(cmd.args, cmd.event, cmd.server, cmd.user, cmd.engine);
                }
            }
        }
    }

    public void handlePrivateCommand(DiscCommandParser.clientCommandContainer cmd) {

        if (commands.containsKey(cmd.invoke)) {

            boolean exe = commands.get(cmd.invoke).calledPrivate(cmd.args, cmd.event, cmd.user, cmd.engine);

            if (exe) {
                String args0 = "";
                try {
                    args0 = cmd.args[0];
                } catch (Exception ignored) {
                }

                if (args0.equalsIgnoreCase("help")) {
                    cmd.engine.getDiscEngine().getTextUtils().sendHelp(commands.get(cmd.invoke).help(cmd.engine), cmd.event.getChannel());
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
