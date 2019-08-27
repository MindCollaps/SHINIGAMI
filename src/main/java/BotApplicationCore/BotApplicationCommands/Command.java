package BotApplicationCore.BotApplicationCommands;

import BotApplicationCore.BotApplicationFiles.BotApplicationServer;
import BotApplicationCore.BotApplicationFiles.BotApplicationUser;
import Engines.Engine;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public interface Command {

    boolean calledServer(String[] args, GuildMessageReceivedEvent event, BotApplicationServer server, BotApplicationUser user, Engine engine);
    void actionServer(String[] args, GuildMessageReceivedEvent event, BotApplicationServer server, BotApplicationUser user, Engine engine);
    boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, BotApplicationUser user, Engine engine);
    void actionPrivate(String[] args, PrivateMessageReceivedEvent event, BotApplicationUser user, Engine engine);
    String help(Engine engine);
}
