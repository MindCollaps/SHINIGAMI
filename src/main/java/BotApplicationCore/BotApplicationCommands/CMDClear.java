package BotApplicationCore.BotApplicationCommands;

import BotApplicationCore.BotApplicationFiles.BotApplicationServer;
import BotApplicationCore.BotApplicationFiles.BotApplicationUser;
import Engines.Engine;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.List;

public class CMDClear implements Command {
    @Override
    public boolean calledServer(String[] args, GuildMessageReceivedEvent event, BotApplicationServer server, BotApplicationUser user, Engine engine) {
        return engine.getBotEngine().getUtilityBase().userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel());
    }

    @Override
    public void actionServer(String[] args, GuildMessageReceivedEvent event, BotApplicationServer server, BotApplicationUser user, Engine engine) {
        int numb = getInt(args[0]);
        if (args.length < 1) {
            engine.getBotEngine().getTextUtils().sendError("Please enter a number of messages you want to delete!", event.getChannel(), true);
        }
        if (numb > 1 && numb <= 1000) {
            try {
                MessageHistory history = new MessageHistory(event.getChannel());
                List<Message> msgs = null;

                event.getMessage().delete().queue();
                try {
                    msgs = history.retrievePast(numb).complete();
                } catch (Exception ignored){
                }
                try{
                    event.getChannel().deleteMessages(msgs).queue();
                } catch (Exception ignored){
                }
                engine.getBotEngine().getTextUtils().sendSucces("Erfolgreich "+ numb + " Nachrichten gelöscht!", event.getChannel(), engine.getProperties().getMiddleTime());

            } catch (Exception e) {
            }
        } else {
            engine.getBotEngine().getTextUtils().sendError("please enter a number of messages between 2 and 1000!", event.getChannel(), true);
        }
    }

    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, BotApplicationUser user, Engine engine) {
        return false;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event, BotApplicationUser user, Engine engine) {

    }

    @Override
    public String help(Engine engine) {
        return "clear [size] - clears a defined amount of messages";
    }

    private int getInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
