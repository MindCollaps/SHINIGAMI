package botApplications.discApplicationCore.discApplicationCommands;

import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationServer;
import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationUser;
import engines.Engine;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.awt.*;

public class DiscCMDLearn implements DicCommand {
    @Override
    public boolean calledServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        //if(server.isLearnServer()){
        return true;

            /*
        } else {
            engine.getDiscEngine().getTextUtils().sendError("Dieser Server ist nicht als Lernserver registriert!", event.getChannel(), engine.getProperties().getMiddleTime(), false);
            return false;
        }
        */
    }

    @Override
    public void actionServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "add":
                    if (args.length > 0) {
                        switch (args[1]) {
                            case "apm":
                                String date;
                                String headline = "";
                                String subject;
                                String description = "";

                                if (args.length > 4) {
                                    date = args[2];
                                    headline = args[3];
                                    subject = args[4];
                                    for (int i = 5; i < args.length; i++) {
                                        description = description + " " + args[i];
                                    }
                                    try {
                                        engine.getDiscEngine().getFilesHandler().getServerById(event.getGuild().getId()).addAppointment(date + " " + headline + " " + subject + " " + description);
                                        engine.getDiscEngine().getTextUtils().sendSucces("Termin " + headline + " erfolgreich erstellt!", event.getChannel(), engine.getProperties().getMiddleTime());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        engine.getDiscEngine().getTextUtils().sendError("Dieser Server hat keinen Apm Channel oder ist nicht registriertl!", event.getChannel(), engine.getProperties().getMiddleTime(), false);
                                    }
                                }
                                break;
                        }
                    }
                    break;

                case "remove":

                    if (args.length > 0) {
                        switch (args[1].toLowerCase()) {
                            case "apmchannel":
                                try {
                                    engine.getDiscEngine().getFilesHandler().getServerById(event.getGuild().getId()).setAppointmentChannel("");
                                } catch (Exception e) {
                                    engine.getDiscEngine().getTextUtils().sendError("Dieser Server hat keinen Apm Channel!", event.getChannel(), engine.getProperties().getMiddleTime(), false);
                                    return;
                                }
                                engine.getDiscEngine().getTextUtils().sendSucces("Der Channel wurde entfernt!", event.getChannel(), engine.getProperties().getMiddleTime());
                                break;
                        }
                    }
                    break;

                case "set":
                    if (args.length > 0) {
                        switch (args[1].toLowerCase()) {
                            case "apmchannel":
                                if (args.length > 1) {
                                    TextChannel tc;
                                    DiscApplicationServer s = null;
                                    try {
                                        tc = event.getGuild().getTextChannelById(args[2]);
                                    } catch (Exception e) {
                                        engine.getDiscEngine().getTextUtils().sendError("Invalid Channel!", event.getChannel(), engine.getProperties().getMiddleTime(), false);
                                        return;
                                    }
                                    try {
                                        s = engine.getDiscEngine().getFilesHandler().getServers().get(event.getGuild().getId());
                                    } catch (Exception e) {
                                        try {
                                            engine.getDiscEngine().getFilesHandler().createNewServer(event.getGuild());
                                        } catch (Exception ex) {
                                            ex.printStackTrace();   //KANN NICHT PASSIEREN O-O eigentlich
                                            return;
                                        }
                                    }
                                    s.setAppointmentChannel(tc.getId());
                                    engine.getDiscEngine().getTextUtils().sendSucces("Dieser Channel wird nun als Terminchannel verwendet!", tc, engine.getProperties().getMiddleTime());
                                } else {
                                    engine.getDiscEngine().getTextUtils().sendError("Invalid Channel!", event.getChannel(), engine.getProperties().getMiddleTime(), false);
                                }
                                break;
                        }
                    }
                    break;

                case "print":
                case "renew":
                case "reload":
                case "rl":
                    renewApmChannel(server, event, engine);
                    break;
            }
        }
    }

    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
        engine.getDiscEngine().getTextUtils().sendError("Dieser command funktioniert nur auf Servern!", event.getChannel(), false);
        return false;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {

    }

    @Override
    public String help(Engine engine) {
        return "-learn add apm <datum> <headline> <Fach> <Beschreibung> - Erstellt neuen Termin\n" +
                "-learn set apmchannel <channel id> - Stellt den Termin Channel ein\n" +
                "-learn [print/renew/reload/rl] - Lädt den Terminchannel neu";
    }

    @Override
    public void actionTelegram(Member member, Engine engine, DiscApplicationUser user, String[] args) {
        //Nothing
    }

    private void renewApmChannel(DiscApplicationServer server, GuildMessageReceivedEvent event, Engine engine) {
        String currentApm = "";
        Object[] sortApms = server.getAppointments().toArray();
        for (int i = 1; i < sortApms.length; i++) {
            for (int j = 0; j < sortApms.length - 1; j++) {
                try {
                    if (parseDate((String) sortApms[j]) > parseDate((String) sortApms[j + 1])) {
                        currentApm = (String) sortApms[j];
                        sortApms[j] = sortApms[j + 1];
                        sortApms[j + 1] = currentApm;
                    }
                } catch (Exception e) {
                    System.err.println("---following error is caused by Date parse error---");
                    e.printStackTrace();
                }
            }

        }

        String[] apms = new String[sortApms.length];
        for (int i = 0; i < sortApms.length; i++) {
            apms[i] = (String) sortApms[i];
        }

        for (String apm : apms) {
            engine.getDiscEngine().getTextUtils().sendCustomMessage(buildAppointment(apm), event.getGuild().getTextChannelById(server.getAppointmentChannel()), "Appointment", Color.BLUE);
        }
    }

    private int parseDate(String date) throws Exception{
        String da = date.split("\\.")[2].substring(0, 4) + date.split("\\.")[1] + date.split("\\.")[0];
        return Integer.valueOf(da);
    }

    private String buildAppointment(String raw) {
        String[] parts = raw.split(" ");
        String des = "";
        for (int i = 3; i < parts.length; i++) {
            des = des + " " + parts[i];
        }
        return "**" + parts[1] + "**\n`Fach: " + parts[2] + "`\n`Datum: " + parts[0] + "`\n\n`" + des + "`";
    }
}