package botApplications.discApplicationCore.discApplicationCommands;

import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationServer;
import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationUser;
import engines.Engine;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.List;

public class DiscCMDClear implements DicCommand {
    @Override
    public boolean calledServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        return engine.getDiscEngine().getUtilityBase().userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel());
    }

    @Override
    public void actionServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        int numb = getInt(args[0]);
        if (args.length < 1) {
            engine.getDiscEngine().getTextUtils().sendError("Please enter a number of messages you want to delete!", event.getChannel(), true);
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
                engine.getDiscEngine().getTextUtils().sendSucces("Erfolgreich "+ numb + " Nachrichten gelöscht!", event.getChannel(), engine.getProperties().getMiddleTime());

            } catch (Exception e) {
            }
        } else {
            engine.getDiscEngine().getTextUtils().sendError("please enter a number of messages between 2 and 1000!", event.getChannel(), true);
        }
    }

    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
        return false;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {

    }

    @Override
    public String help(Engine engine) {
        return "clear [size] - clears a defined amount of messages";
    }

    @Override
    public void actionTelegram(Member member, Engine engine, DiscApplicationUser user, String[] args) {

    }

    private int getInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
