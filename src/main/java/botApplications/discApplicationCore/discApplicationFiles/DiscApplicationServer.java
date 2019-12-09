package botApplications.discApplicationCore.discApplicationFiles;

import net.dv8tion.jda.core.entities.Guild;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscApplicationServer implements Serializable {

    public static final long serialVersionUID = 42L;

    String serverName;
    String serverID;
    String serverYTPlaylist;
    boolean listenerEnabled = false;
    boolean musicListenerEnabled = false;
    String musicListenerName = "djNexus";

    //lernserverStuff
    boolean learnServer = false;
    String appointmentChannel;
    ArrayList appointments = new ArrayList();   //date + " " + headline + " " + subject + " " + description

    public DiscApplicationServer(Guild guild) {
        this.serverName = guild.getName();
        this.serverID = guild.getId();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getServerYTPlaylist() {
        return serverYTPlaylist;
    }

    public void setServerYTPlaylist(String serverYTPlaylist) {
        this.serverYTPlaylist = serverYTPlaylist;
    }

    public boolean isListenerEnabled() {
        return listenerEnabled;
    }

    public void setListenerEnabled(boolean listenerEnabled) {
        this.listenerEnabled = listenerEnabled;
    }

    public boolean isMusicListenerEnabled() {
        return musicListenerEnabled;
    }

    public void setMusicListenerEnabled(boolean musicListenerEnabled) {
        this.musicListenerEnabled = musicListenerEnabled;
    }

    public String getMusicListenerName() {
        return musicListenerName;
    }

    public void setMusicListenerName(String musicListenerName) {
        this.musicListenerName = musicListenerName;
    }

    public boolean isLearnServer() {
        return learnServer;
    }

    public void setLearnServer(boolean learnServer) {
        this.learnServer = learnServer;
    }

    public String getAppointmentChannel() {
        return appointmentChannel;
    }

    public void setAppointmentChannel(String appointmentChannel) {
        this.appointmentChannel = appointmentChannel;
    }

    public ArrayList getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(String appointment){
        this.appointments.add(appointment);
    }

    public void initNewOnes(){
        try {
            if(learnServer != true)
                learnServer = false;
        } catch (Exception e){
            learnServer = false;
        }
        try {
            if (appointmentChannel.length()>=0)
                appointmentChannel = "";
        } catch (Exception e){
            appointmentChannel = "";
        }
        try {
            if(appointments.size()>=0)
                appointments = new ArrayList();
        } catch (Exception e){
            appointments = new ArrayList();
        }
    }
}
