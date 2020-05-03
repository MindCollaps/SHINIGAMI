package planningBotCore.librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class Subject implements Serializable {

    public static final long serialVersionUID = 42L;

    private String subjectName = "";
    private ArrayList<Teacher> subjectTeacher = new ArrayList<>();
    private School school;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public ArrayList<Teacher> getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(ArrayList<Teacher> subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public void addTeacher(Teacher teacher){
        subjectTeacher.add(teacher);
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}