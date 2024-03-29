package botApplications.teleApplicationCore;

import engines.Engine;
import com.pengrad.telegrambot.model.Update;

import java.util.ArrayList;

public class TeleCommandParser {

    public CommandContainer parseCommand(String raw, Engine engine, Update update){
        String beheaded = raw.replaceFirst("/", "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        for (String s : splitBeheaded) {
            split.add(s);
        }

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new CommandContainer(raw,beheaded,splitBeheaded,invoke,args,update,engine);
    }


    public class CommandContainer{
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final Update update;
        public final Engine engine;

        public CommandContainer(String raw, String beheaded, String[] splitBeheaded, String invoke, String[] args, Update update, Engine engine) {
            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.update = update;
            this.engine = engine;
        }
    }
}
