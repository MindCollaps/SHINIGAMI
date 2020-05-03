package planningBotCore.librarys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Appointment implements Serializable {

    public static final long serialVersionUID = 42L;

    private String description = "";
    private Date creationDate;
    private Date submissionDate;
    private ArrayList<AppointmentFile> appointmentFiles = new ArrayList<>();

    private boolean isSchoolAppointment = false;
    private SchoolAppointment schoolAppointment;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public boolean isSchoolAppointment() {
        return isSchoolAppointment;
    }

    public void setSchoolAppointment(boolean schoolAppointment) {
        isSchoolAppointment = schoolAppointment;
    }

    public SchoolAppointment getSchoolAppointment() {
        return schoolAppointment;
    }

    public void setSchoolAppointment(SchoolAppointment schoolAppointment) {
        this.schoolAppointment = schoolAppointment;
    }
}