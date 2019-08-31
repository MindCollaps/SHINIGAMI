package BotApplications.DiscApplicationCore.DiscApplicationCommands;

import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationServer;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationUser;
import Engines.Engine;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class CMDInfo implements Command {

    @Override
    public boolean calledServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        return true;
    }

    @Override
    public void actionServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        engine.getDiscEngine().getTextUtils().sendSucces("ThisBot\n\n`Name: "+ engine.getProperties().getBotApplicationName() + "`\n\n`Version: " + engine.getProperties().getBotApplicationVersion() + "`\n\n`Develop state: \"WE\" are still coding ufff`\n\n`Developer: Noah Elijah Till (Neo_MCノア)`", event.getChannel(), engine.getProperties().getVeryLongTime());
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
        return "Dieser command zeigt dir ein paar dinge über mich uwu";
    }
}
