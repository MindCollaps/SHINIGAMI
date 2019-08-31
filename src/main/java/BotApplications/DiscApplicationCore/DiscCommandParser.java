package BotApplications.DiscApplicationCore;

import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationServer;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationUser;
import Engines.Engine;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.ArrayList;

public class DiscCommandParser {

    public DiscCommandParser(Engine engine) {
    }

    public serverCommandContainer parseServerMessage(String raw, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {

        String beheaded = raw.replaceFirst(engine.getProperties().getBotApplicationPrefix(), "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        for (String s : splitBeheaded) {
            split.add(s);
        }

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new serverCommandContainer(raw, beheaded, splitBeheaded, invoke, args, event, server, user, engine);
    }

    public clientCommandContainer parseClientMessage(String raw, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {

        String beheaded = raw.replaceFirst(engine.getProperties().getBotApplicationPrefix(), "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        for (String s : splitBeheaded) {
            split.add(s);
        }

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new clientCommandContainer(raw, beheaded, splitBeheaded, invoke, args, event, user, engine);
    }


    public class serverCommandContainer {

        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final GuildMessageReceivedEvent event;
        public final DiscApplicationServer server;
        public final DiscApplicationUser user;
        public final Engine engine;

        public serverCommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
            this.raw = rw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
            this.server = server;
            this.user = user;
            this.engine = engine;
        }

    }

    public class clientCommandContainer {
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final PrivateMessageReceivedEvent event;
        public final DiscApplicationUser user;
        public final Engine engine;

        public clientCommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
            this.raw = rw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
            this.user = user;
            this.engine = engine;
        }
    }
}
