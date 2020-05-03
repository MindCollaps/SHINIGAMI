package engines;

import planningBotCore.librarys.Appointment;
import planningBotCore.librarys.School;
import planningBotCore.librarys.User;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PlanningEngine {

    private Engine engine;

    private ArrayList<Appointment> appointments = new ArrayList<>();
    private ArrayList<School> schools = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    private final String dirDest = "/bot/planning";

    public PlanningEngine(Engine engine) {
        this.engine = engine;
    }

    public void boot(){
        System.out.println("Planinng engine Start...");
        System.out.println("Loading files");
        loadAppointments();
        loadSchools();
        loadUsers();
        System.out.println("Planning engine start succesfull!");
    }

    public void loadAppointments(){
        try {
            appointments = (ArrayList<Appointment>) engine.getFileUtils().loadObject(engine.getFileUtils().home + dirDest + "/appointments.apm");
        } catch (Exception e) {
            engine.getUtilityBase().printDebug("appointments can't load!");
            if(e instanceof FileNotFoundException)
                engine.getUtilityBase().printDebug("-Not found exception, maybe never created?");
        }
    }

    public void loadSchools(){
        try {
            schools = (ArrayList<School>) engine.getFileUtils().loadObject(engine.getFileUtils().home + dirDest + "/schools.sc");
        } catch (Exception e) {
            engine.getUtilityBase().printDebug("schools can't load!");
            if(e instanceof FileNotFoundException)
                engine.getUtilityBase().printDebug("-Not found exception, maybe never created?");
        }
    }

    public void loadUsers(){
        try {
            users = (ArrayList<User>) engine.getFileUtils().loadObject(engine.getFileUtils().home + dirDest + "/users.us");
        } catch (Exception e) {
            engine.getUtilityBase().printDebug("users can't load!");
            if(e instanceof FileNotFoundException)
                engine.getUtilityBase().printDebug("-Not found exception, maybe never created?");
        }
    }

    public void safeAppointments(){
        try {
            engine.getFileUtils().saveOject(engine.getFileUtils().home + dirDest + "/appointments.apm", appointments);
        } catch (Exception e) {
            e.printStackTrace();
            engine.getUtilityBase().printDebug("Appointments can't save...");
        }
    }

    public void safeSchools(){
        try {
            engine.getFileUtils().saveOject(engine.getFileUtils().home + dirDest + "/schools.sc", schools);
        } catch (Exception e) {
            e.printStackTrace();
            engine.getUtilityBase().printDebug("Schools can't save...");
        }
    }

    public void safeUsers(){
        try {
            engine.getFileUtils().saveOject(engine.getFileUtils().home + dirDest + "/users.us", users);
        } catch (Exception e) {
            e.printStackTrace();
            engine.getUtilityBase().printDebug("Users can't save...");
        }
    }
}
