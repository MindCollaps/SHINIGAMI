package botApplications.discApplicationCore.discApplicationCommands;

import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationServer;
import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationUser;
import engines.Engine;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class DiscCMDHelp implements DicCommand {
    @Override
    public boolean calledServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        return true;
    }

    @Override
    public void actionServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        sendServerHelp(event,engine);
    }

    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
        return true;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
        sendPrivateHelp(event,engine);
    }

    @Override
    public String help(Engine engine) {
        return null;
    }

    @Override
    public void actionTelegram(Member member, Engine engine, DiscApplicationUser user, String[] args) {

    }

    private void sendServerHelp(GuildMessageReceivedEvent event, Engine engine){
        engine.getDiscEngine().getTextUtils().sendHelp("\n**Help: **\n" +
                "\nUm Hilfe zu den einzelnen Commands zu erhalten, einfach den Command und dann help dahinter schreiben ;3" +
                "\n-m (Musik)" +
                "\n-info (Infos zum Bot und entwicklung)" +
                "\n-clear (Chat leeren)", event.getChannel());
    }

    private void sendPrivateHelp(PrivateMessageReceivedEvent event, Engine engine){
        engine.getDiscEngine().getTextUtils().sendHelp("Für diesen bot gibt es im Moment keine privaten commands :3 aber du kannst mit mir reden, du musst mich nur ansprechen :333", event.getChannel());
    }
}
