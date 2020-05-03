package planningBotCore.librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class Teacher implements Serializable {

    public static final long serialVersionUID = 42L;

    private ArrayList<Subject> teachedSubjects = new ArrayList<>();

    public ArrayList<Subject> getTeachedSubjects() {
        return teachedSubjects;
    }

    public void setTeachedSubjects(ArrayList<Subject> teachedSubjects) {
        this.teachedSubjects = teachedSubjects;
    }

    public void addSubject(Subject subject){
        teachedSubjects.add(subject);
    }
}
