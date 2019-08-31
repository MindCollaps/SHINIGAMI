package BotApplications.DiscApplicationCore.DiscApplicationListeners;

import Engines.Engine;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;

public class ServerJoinListener extends Listener {

    public ServerJoinListener(Engine engine) {
        super(engine);
    }

    public void onGuildJoin(GuildJoinEvent event) {
        engine.getDiscEngine().getTextUtils().sendWellcomeMessage(":robot: Hallo ich bin neu hier :wave: \n\nIch heiße Shinigami!\n\nDamit ich richtig funktioniere gebe mir bitte eine Rolle mit dem rang Admin, sonst funktioniere ich nicht richtig!", event.getGuild().getTextChannels().get(0));
    }
}
