package botApplications.teleApplicationCore.teleApplicationCommands;

import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationUser;
import engines.Engine;
import com.pengrad.telegrambot.model.Update;
import net.dv8tion.jda.core.entities.*;

public class TeleCMDDiscordConnection implements TeleCommand {

    @Override
    public boolean called(Update command, Engine engine, String[] args) {
        return true;
    }

    @Override
    public void action(Update command, Engine engine, String[] args) {
        DiscApplicationUser user = null;
        try {
            user = engine.getTeleApplicationEngine().getUserByTelegramId(command.message().chat().id());
        } catch (Exception e) {
        }
        switch (args[0]){
            case "connect":
                if(engine.getTeleApplicationEngine().discordConnectionContaisId(args[1])){
                    engine.getTeleApplicationEngine().getTextUtils().sendMessage(command.message().chat().id(), "Du wurdest anscheinend schon regestriert :3");
                    return;
                }
                if(!engine.getDiscEngine().getBotJDA().getUserById(args[1]).hasPrivateChannel()){
                    engine.getDiscEngine().getBotJDA().getUserById(args[1]).openPrivateChannel().queue();
                }
                PrivateChannel channel;
                for (int i = 0; i < engine.getDiscEngine().getBotJDA().getPrivateChannels().size(); i++) {
                    channel = engine.getDiscEngine().getBotJDA().getPrivateChannels().get(i);
                    if(channel.getUser().getId().equals(args[1])){
                        engine.getDiscEngine().getTextUtils().sendWarining("**TELEGRAM**\n\nUser: " + command.message().chat().username() + " versucht deinen Discord Account mit Telegram zu verbinden. Ist das oke?\n\n" + engine.getProperties().getDiscBotApplicationPrefix() + "telegram accept", channel);
                        engine.getTeleApplicationEngine().createNewDiscordConnectionRequest(args[1], command.message().chat().id());
                        engine.getTeleApplicationEngine().getTextUtils().sendMessage(command.message().chat().id(), "Eine anfrage wurde an deinen Discord Account geschickt!");
                        return;
                    }
                }
                engine.getTeleApplicationEngine().getTextUtils().sendMessage(command.message().chat().id(), "Du hast noch nie mit dem Discord Bot geredet, schick ihm doch mal eine nachricht!");
                break;

            case "music":
                if(user==null){
                    engine.getTeleApplicationEngine().getTextUtils().sendMessage(user.getTelegramId(), "Du hast aktuell keine Verbindung mit Discord aufgebaut, um mehr zu erfahren versuche /discord help");
                } else {
                    String[] newArsgs = new String[args.length-1];
                    int count = 0;
                    for (int i = 1; i < args.length; i++) {
                        newArsgs[count] = args[i];
                        count ++;
                    }
                    Guild g = null;
                    Member member = null;
                    for (int i = 0; i < engine.getDiscEngine().getBotJDA().getGuilds().size(); i++) {
                        Guild current = engine.getDiscEngine().getBotJDA().getGuilds().get(i);
                        Member currentMember = current.getMemberById(user.getUserId());
                        if(currentMember==null){
                            continue;
                        }
                        if(currentMember.getVoiceState().getChannel()!=null){
                            g = current;
                            member = currentMember;
                            break;
                        }
                    }

                    if(g==null){
                        engine.getTeleApplicationEngine().getTextUtils().sendMessage(user.getTelegramId(), "Dieser Guild ist inkorrekt!!!");
                        return;
                    }
                    engine.getDiscEngine().getDiscCommandHandler().commands.get("m").actionTelegram(member, engine, user, newArsgs);
                }
                break;
        }
    }

    @Override
    public String help(Engine engine, String[] args) {
        return "connect <userID> - links your telegram to your discord account!\n" +
                "music <command> - dispatchs a music command";
    }
}
