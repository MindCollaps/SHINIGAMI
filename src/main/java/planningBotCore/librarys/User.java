package planningBotCore.librarys;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    public static final long serialVersionUID = 42L;

    private String firstName = "";
    private String lastName = "";
    private String username = "";
    private String email = "";
    private String mobileNumber = "";
    private ArrayList<School> visetedSchools = new ArrayList<>();
    private Authorization authorization;
    private Teacher teacher;

    public enum Authorization{
        Admin, User, Teacher
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<School> getVisetedSchools() {
        return visetedSchools;
    }

    public void setVisetedSchools(ArrayList<School> visetedSchools) {
        this.visetedSchools = visetedSchools;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
