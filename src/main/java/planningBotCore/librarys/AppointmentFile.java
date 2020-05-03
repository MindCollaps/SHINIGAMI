package planningBotCore.librarys;

import java.io.File;
import java.io.Serializable;

public class AppointmentFile implements Serializable {

    public static final long serialVersionUID = 42L;

    private String url = "";
    private String destination = "";

    public File getFile(){
        //TODO: download file or copy file from someware as a File
        if(!destination.equals("")){

        }
        if(!url.equals("")){

        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}