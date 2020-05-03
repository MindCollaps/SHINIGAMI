package planningBotCore.librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class School implements Serializable {

    public static final long serialVersionUID = 42L;

    private String schoolName = "";
    private int plz = 0;
    private String country = "";
    private String street = "";
    private String houseNumber = "";
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();

    public String getSchoolName() {
        return schoolName;
    }

    public int getPlz() {
        return plz;
    }

    public String getCountry() {
        return country;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment){
        appointments.add(appointment);
    }

    public void addSubject(Subject subject){
        subjects.add(subject);
    }

    public void addTeacher(Teacher teacher){
        teachers.add(teacher);
    }
}
