package BotApplications.DiscApplicationCore.DiscApplicationListeners;

import Engines.Engine;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class ServerMessageListener extends Listener {


    public ServerMessageListener(Engine engine) {
        super(engine);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if(event.getMessage().getContentRaw().length()>400){
            return;
        }

        handleSecret(event);

        Member selfUser = event.getGuild().getMemberById(event.getGuild().getJDA().getSelfUser().getId());
        boolean commandWorked = false;

        //normal command
        //prefix check and self user
        //permission check
        if (!event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + " message listener received guild message!");
                    if (event.getMessage().getContentRaw().startsWith(engine.getProperties().getDiscBotApplicationPrefix())) {
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
                                    engine.getDiscEngine().getUtilityBase().sendGuildCommand(event);
                                    event.getMessage().delete().queue();
                                    commandWorked = true;
                                    break;
                                }
                            }
                            if (!commandWorked) {
                                engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + "command " + event.getMessage().getContentRaw() + " doesnt exist!");
                                engine.getDiscEngine().getTextUtils().deletUserMessage(1, event);
                                engine.getDiscEngine().getTextUtils().sendError("DicCommand " + event.getMessage().getContentRaw() + "  existiert nicht!\n\nSchreibe **" + engine.getProperties().getDiscBotApplicationPrefix() + "help** um eine auflistung der Commands zu erhalten.", event.getChannel(), engine.getProperties().getMiddleTime(), true);
                            }
                        } else {
                            engine.getUtilityBase().printDebug(messageInfo(event.getGuild()) + " bot has not the permission!");
                            engine.getDiscEngine().getTextUtils().sendError("Der bot hat nicht die nötigen berechtigungen!\n\nBitte weise ihm die Admin rechte zu!", event.getChannel(), engine.getProperties().getLongTime(), true);
                        }
                return;
            } else {
                //CHANGE HERE THE UNDERSTANDINGCHANNEL
                //Check bot invoke
                if(event.getMessage().getContentRaw().toLowerCase().startsWith("shini")) {
                    engine.getAiEngine().runAiForDiscord(event);
                }
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().length()>400){
            return;
        }
        boolean commandWorked = false;

        //normal command
        //prefix check and self user
        //permission check
        if (!event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            engine.getUtilityBase().printDebug(" message listener received guild message!");
            if (event.getMessage().getContentRaw().startsWith(engine.getProperties().getDiscBotApplicationPrefix())) {
                    //command exist check
                    for (int i = 0; engine.getDiscEngine().getDiscCommandHandler().commandIvokes.size() > i; i++) {
                        if (event.getMessage().getContentRaw().contains(engine.getDiscEngine().getDiscCommandHandler().commandIvokes.get(i))) {
                            engine.getDiscEngine().getUtilityBase().sendPrivateCommand(event);
                            commandWorked = true;
                            break;
                        }
                    }
                    if (!commandWorked) {
                        engine.getDiscEngine().getTextUtils().sendError("DicCommand " + event.getMessage().getContentRaw() + "  existiert nicht!\n\nSchreibe **" + engine.getProperties().getDiscBotApplicationPrefix() + "help** um eine auflistung der Commands zu erhalten.", event.getChannel(), engine.getProperties().getMiddleTime(), true);
                    }
                return;
            } else {
                //CHANGE HERE THE UNDERSTANDINGCHANNEL
                //Check bot invoke
                //if(event.getMessage().getContentRaw().toLowerCase().startsWith("shini")) {
                 //   engine.getAiEngine().runAiForDiscord(event);
                //}
            }
        }
    }

    private String messageInfo(Guild guild) {
        return "[serverMessageListener -" + guild.getName() + "|" + guild.getId() + "]";
    }

    private String handleSecret(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.length() > 3) {
            String[] msgArgs = message.split(" ");

            //contains
            for (int i = 0; msgArgs.length > i; i++) {
                switch (msgArgs[i].toLowerCase()) {
                    case "lol":
                    case "lool":
                        sendNormalText(event, "Ja loool eyyy was ist lol? XD");
                        break;
                    case "anime":
                        sendNormalText(event, "Who said Anime :O");
                        break;
                    case "baka":
                        sendNormalText(event, "Baaaaaaaaaaaaaka! XD");
                        break;
                    case "jaja":
                        sendNormalText(event, "JAJA HEIßT LECK MICH AM ARSCH XDXDXD :D ");
                        break;
                    case "aloha":
                        sendNormalText(event, "Aloha " + event.getAuthor().getName() + " XD \nDas hat mir der Mosel beigebracht ");
                        break;
                }
            }
        }
        return null;

    }

    private void sendNormalText(GuildMessageReceivedEvent event, String Message) {
        engine.getDiscEngine().getTextUtils().sendNormalTxt(Message, event.getChannel());
    }
}
