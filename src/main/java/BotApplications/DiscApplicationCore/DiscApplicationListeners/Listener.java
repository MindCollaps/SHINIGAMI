package BotApplications.DiscApplicationCore.DiscApplicationListeners;

import Engines.Engine;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {

     Engine engine;

    public Listener(Engine engine) {
        this.engine = engine;
    }
}
