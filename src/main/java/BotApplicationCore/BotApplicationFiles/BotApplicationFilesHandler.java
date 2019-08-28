package BotApplicationCore.BotApplicationFiles;

import Engines.Engine;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;

public class BotApplicationFilesHandler {

    HashMap<String, BotApplicationUser> users = new HashMap<>();
    HashMap<String, BotApplicationServer> servers = new HashMap<>();

    Engine engine;

    public BotApplicationFilesHandler(Engine engine) {
        this.engine = engine;
    }

    public BotApplicationUser getUserById(String id) throws Exception {
        BotApplicationUser user = null;
        if(users.containsKey(id)){
            user = users.get(id);
        }
        return user;
    }

    public BotApplicationServer getServerById(String id) throws Exception {
        BotApplicationServer server = null;
        if(servers.containsKey(id)){
            server = servers.get(id);
        }
        return server;
    }

    public BotApplicationServer createNewServer(Guild guild) throws Exception{
        if(servers.containsKey(guild.getId())){
            engine.getUtilityBase().printDebug("Cant create new server because already exist! Id: " + guild.getId() + " name: " + guild.getName());
            throw new Exception("Serer already exist");
        }
        BotApplicationServer server = new BotApplicationServer(guild);
        servers.put(guild.getId(), server);
        return server;
    }

    public BotApplicationUser createNewUser(User user) throws Exception{
        if(users.containsKey(user.getId())){
            engine.getUtilityBase().printDebug("Cant create new server because already exist! Id: " + user.getId() + " name: " + user.getName());
            throw new Exception("User already exist");
        }
        BotApplicationUser botUser = new BotApplicationUser(user);
        users.put(user.getId(), botUser);
        return botUser;
    }

    public void loadAllBotFiles(){
        System.out.println("~load all bot files!");
        try {
            servers = (HashMap<String, BotApplicationServer>) engine.getFileUtils().loadObject(engine.getFileUtils().getHome() + "/bot/utilize/servers.server");
        } catch (Exception e) {
            System.out.println("!!Servers cant load!!");
        }
        try {
            users = (HashMap<String, BotApplicationUser>) engine.getFileUtils().loadObject(engine.getFileUtils().getHome() + "/bot/utilize/users.users");
        } catch (Exception e) {
            System.out.println("!!Users cant load!!");
        }
        System.out.println("~finished loading bot files");
    }

    public void saveAllBotFiles(){
        System.out.println("~safe all bot files!");
        engine.getFileUtils().saveOject(engine.getFileUtils().getHome() + "/bot/utilize/users.users", users);
        engine.getFileUtils().saveOject(engine.getFileUtils().getHome() + "/bot/utilize/servers.server", servers);
        System.out.println("~finished saving all bot files");
    }
}
