package planningBotCore.librarys;

import java.io.Serializable;

public class SchoolAppointment implements Serializable {

    public static final long serialVersionUID = 42L;

    private Teacher teacher;
    private Subject subject;
    private School school;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
