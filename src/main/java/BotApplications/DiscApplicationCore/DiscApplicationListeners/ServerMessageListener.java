package BotApplications.DiscApplicationCore.DiscApplicationListeners;

import Engines.Engine;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ServerMessageListener extends ListenerAdapter {

    Engine engine;

    public ServerMessageListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + " message listener received message!");

        Member selfUser = event.getGuild().getMemberById(event.getGuild().getJDA().getSelfUser().getId());
        boolean commandWorked = false;


        //normal command
        //prefix check and self user
        //permission check
        if (!event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                    if (event.getMessage().getContentRaw().startsWith(engine.getProperties().getBotApplicationPrefix())) {
                        boolean hasPermission = false;
                        try {
                            hasPermission = selfUser.hasPermission(Permission.ADMINISTRATOR);
                        } catch (Exception e) {
                            engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + " Bot has no permissions");
                        }

                        if (hasPermission) {
                            //command exist check
                            for (int i = 0; engine.getDiscEngine().getDiscCommandHandler().commandIvokes.size() > i; i++) {
                                if (event.getMessage().getContentRaw().contains(engine.getDiscEngine().getDiscCommandHandler().commandIvokes.get(i))) {
                                    engine.getDiscEngine().getUtilityBase().sendCommand(event);
                                    event.getMessage().delete().queue();
                                    commandWorked = true;
                                    break;
                                }
                            }
                            if (!commandWorked) {
                                engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + "command " + event.getMessage().getContentRaw() + " doesnt exist!");
                                engine.getDiscEngine().getTextUtils().deletUserMessage(1, event);
                                engine.getDiscEngine().getTextUtils().sendError("Command " + event.getMessage().getContentRaw() + "  existiert nicht!\n\nSchreibe **" + engine.getProperties().getBotApplicationPrefix() + "help** um eine auflistung der Commands zu erhalten.", event.getChannel(), engine.getProperties().getMiddleTime(), true);
                            }
                        } else {
                            engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + " bot has not the permission!");
                            engine.getDiscEngine().getTextUtils().sendError("Der bot hat nicht die nötigen berechtigungen!\n\nBitte weise ihm die Admin rechte zu!", event.getChannel(), engine.getProperties().getLongTime(), true);
                        }
                return;
            } else {
                //CHANGE HERE THE UNDERSTANDINGCHANNEL
                //Check bot invoke
                if(event.getMessage().getContentRaw().startsWith("shini")) {
                    engine.getAiEngine().runAi(event);
                }
            }
        }
    }

    private String messageInfo(Guild guild) {
        return "[serverMessageListener -" + guild.getName() + "|" + guild.getId() + "]";
    }
}
