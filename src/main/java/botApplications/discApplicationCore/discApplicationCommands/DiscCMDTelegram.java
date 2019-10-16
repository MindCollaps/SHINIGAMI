package botApplications.discApplicationCore.discApplicationCommands;

import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationServer;
import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationUser;
import engines.Engine;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class DiscCMDTelegram implements DicCommand {

    @Override
    public boolean calledServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        return false;
    }

    @Override
    public void actionServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {

    }

    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
        if(!engine.getTeleApplicationEngine().discordRequestContainsId(event.getAuthor().getId())){
            engine.getDiscEngine().getTextUtils().sendError("Du bist anscheinend nicht verbunden! Mache dies über den Telegramm bot!", event.getChannel(), false);
            return false;
        }
        return true;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
        switch (args[0]){
            case "accept":
                engine.getTeleApplicationEngine().confirmRequest(event.getAuthor().getId(), event.getChannel());
                break;

            case "disconnect":
                engine.getTeleApplicationEngine().removeDiscordConnection(event.getAuthor().getId());
                engine.getDiscEngine().getTextUtils().sendSucces("Deine Connection wurde aufgehoben!" , event.getChannel());
                break;
        }
    }

    @Override
    public String help(Engine engine) {
        return "disconnect - disconnects your connection with Telegram!";
    }

    @Override
    public void actionTelegram(Member member, Engine engine, DiscApplicationUser user, String[] args) {

    }
}
