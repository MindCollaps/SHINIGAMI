package BotApplications.DiscApplicationCore.DiscApplicationCommands;

import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationServer;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationUser;
import Engines.Engine;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public interface DicCommand {

    boolean calledServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine);
    void actionServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine);
    boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine);
    void actionPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine);
    String help(Engine engine);
    void actionTelegram(Member member, Engine engine, DiscApplicationUser user, String[] args);
}
