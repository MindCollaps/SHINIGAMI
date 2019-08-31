package BotApplications.DiscApplicationCore.DiscApplicationFiles;

import Engines.Engine;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;

public class DiscApplicationFilesHandler {

    HashMap<String, DiscApplicationUser> users = new HashMap<>();
    HashMap<String, DiscApplicationServer> servers = new HashMap<>();

    Engine engine;

    public DiscApplicationFilesHandler(Engine engine) {
        this.engine = engine;
    }

    public DiscApplicationUser getUserById(String id) throws Exception {
        DiscApplicationUser user = null;
        if(users.containsKey(id)){
            user = users.get(id);
        }
        return user;
    }

    public DiscApplicationServer getServerById(String id) throws Exception {
        DiscApplicationServer server = null;
        if(servers.containsKey(id)){
            server = servers.get(id);
        }
        return server;
    }

    public DiscApplicationServer createNewServer(Guild guild) throws Exception{
        if(servers.containsKey(guild.getId())){
            engine.getUtilityBase().printDebug("Cant create new server because already exist! Id: " + guild.getId() + " name: " + guild.getName());
            throw new Exception("Serer already exist");
        }
        DiscApplicationServer server = new DiscApplicationServer(guild);
        servers.put(guild.getId(), server);
        return server;
    }

    public DiscApplicationUser createNewUser(User user) throws Exception{
        if(users.containsKey(user.getId())){
            engine.getUtilityBase().printDebug("Cant create new server because already exist! Id: " + user.getId() + " name: " + user.getName());
            throw new Exception("User already exist");
        }
        DiscApplicationUser botUser = new DiscApplicationUser(user);
        users.put(user.getId(), botUser);
        return botUser;
    }

    public void loadAllBotFiles(){
        System.out.println("~load all bot files!");
        try {
            servers = (HashMap<String, DiscApplicationServer>) engine.getFileUtils().loadObject(engine.getFileUtils().getHome() + "/bot/utilize/servers.server");
        } catch (Exception e) {
            System.out.println("!!Servers cant load!!");
        }
        try {
            users = (HashMap<String, DiscApplicationUser>) engine.getFileUtils().loadObject(engine.getFileUtils().getHome() + "/bot/utilize/users.users");
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
