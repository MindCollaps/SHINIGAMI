package Engines;

import BotApplications.TeleApplicationCore.TeleApplicationListeners.MessageListener;
import BotApplications.TeleApplicationCore.Utils.TeleBotUtils;
import com.pengrad.telegrambot.TelegramBot;

public class TeleApplicationEngine {

    Engine engine;


    TelegramBot bot;

    TeleBotUtils botUtils;

    public TeleApplicationEngine(Engine engine) {
        this.engine = engine;
    }

    public void startBotApplication(){
        botUtils = new TeleBotUtils(engine);

        addCommands();
        bot = new TelegramBot("663797335:AAHBlqwMce8t9LhM0IVoRD4-J33alZNXmB4");
        bot.setUpdatesListener(new MessageListener(engine));
    }

    private void addCommands(){

    }

    public TelegramBot getBot() {
        return bot;
    }

    public TeleBotUtils getBotUtils() {
        return botUtils;
    }
}
