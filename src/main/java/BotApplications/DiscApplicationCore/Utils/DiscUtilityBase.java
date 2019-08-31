package BotApplications.DiscApplicationCore.Utils;

import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationServer;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationUser;
import Engines.Engine;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class DiscUtilityBase {

    Engine engine;

    public DiscUtilityBase(Engine engine) {
        this.engine = engine;
    }

    public boolean userHasGuildAdminPermission(Member member, Guild guild, TextChannel textChannel) {
        boolean hasPermission = false;
        for (int i = 0; member.getRoles().size() > i; i++) {
            for (int a = 0; member.getRoles().get(i).getPermissions().size() > a; i++) {
                if (member.getRoles().get(i).getPermissions().get(a).ADMINISTRATOR != null) {
                    hasPermission = true;
                    break;
                }
            }
        }
        if (hasPermission) {
            return true;
        } else {
            engine.getDiscEngine().getTextUtils().sendError("Du hast keine berechtigung um diesen command auzuführen!", textChannel, engine.getProperties().getMiddleTime(), true);
            return false;
        }
    }

    public void sendOwnCommand(GuildMessageReceivedEvent event, String ownCommand, DiscApplicationUser user, DiscApplicationServer server) {
        engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + " sendOwnCommand(" + event.getMessage().getContentRaw() + ")");

        if (server == null) {
            try {
                server = engine.getDiscEngine().getFilesHandler().createNewServer(event.getGuild());
            } catch (Exception e) {
                System.out.println("Fatal error in ServerMessageListener.sendOwnCommand()---server cant load!!!");
            }
        }

        if (user == null) {
            try {
                user = engine.getDiscEngine().getFilesHandler().createNewUser(event.getAuthor());
            } catch (Exception e) {
                System.out.println("Fatal error in ServerMessageListener.sendOwnCommand()---user cant load!!!");
            }
        }

        if (ownCommand == null || ownCommand.equalsIgnoreCase("")) {
            try {
                engine.getDiscEngine().getDiscCommandHandler().handleServerCommand(engine.getDiscEngine().getDiscCommandParser().parseServerMessage(event.getMessage().getContentRaw(), event, server, user, engine));
            } catch (Exception e) {
                engine.getDiscEngine().getTextUtils().sendError("Fatal command error on command: " + event.getMessage().getContentRaw(), event.getChannel(), true);
                engine.getUtilityBase().printDebug("-----\n[Send server command failed]\n-----");
                e.printStackTrace();
            }
        } else {
            try {
                engine.getDiscEngine().getDiscCommandHandler().handleServerCommand(engine.getDiscEngine().getDiscCommandParser().parseServerMessage(ownCommand, event, server, user, engine));
            } catch (Exception e) {
                engine.getDiscEngine().getTextUtils().sendError("Fatal command error on command: " + event.getMessage().getContentRaw(), event.getChannel(), true);
                engine.getUtilityBase().printDebug("-----\n[Send server command failed]\n-----");
                e.printStackTrace();
            }
        }
    }

    private String messageInfo(Guild guild) {
        return "[send command -" + guild.getName() + "|" + guild.getId() + "]";
    }

    public void sendCommand(GuildMessageReceivedEvent event) {
        engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + " sendOwnCommand(" + event.getMessage().getContentRaw() + ")");

        DiscApplicationUser user = null;
        DiscApplicationServer server = null;

        try {
            server = engine.getDiscEngine().getFilesHandler().getServerById(event.getGuild().getId());
        } catch (Exception e) {
            engine.getUtilityBase().printDebug("![Ai Engine] " + event.getGuild().getId() + " Server not found!");
        }

        try {
            user = engine.getDiscEngine().getFilesHandler().getUserById(event.getAuthor().getId());
        } catch (Exception e) {
            engine.getUtilityBase().printDebug("![Ai Engine] " + event.getAuthor().getId() + " User not found!");
        }

        if (server == null) {
            try {
                server = engine.getDiscEngine().getFilesHandler().createNewServer(event.getGuild());
            } catch (Exception e) {
                System.out.println("Fatal error in ServerMessageListener.sendOwnCommand()---server cant load!!!");
            }
        }

        if (user == null) {
            try {
                user = engine.getDiscEngine().getFilesHandler().createNewUser(event.getAuthor());
            } catch (Exception e) {
                System.out.println("Fatal error in ServerMessageListener.sendOwnCommand()---user cant load!!!");
            }
        }
        try {
            engine.getDiscEngine().getDiscCommandHandler().handleServerCommand(engine.getDiscEngine().getDiscCommandParser().parseServerMessage(event.getMessage().getContentRaw(), event, server, user, engine));
        } catch (Exception e) {
            engine.getDiscEngine().getTextUtils().sendError("Fatal command error on command: " + event.getMessage().getContentRaw(), event.getChannel(), true);
            engine.getUtilityBase().printDebug("-----\n[Send server command failed]\n-----");
            e.printStackTrace();
        }
    }
}
