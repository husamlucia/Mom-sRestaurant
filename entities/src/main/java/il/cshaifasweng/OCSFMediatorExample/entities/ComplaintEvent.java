package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;

import java.io.Serializable;
import java.util.List;

public class ComplaintEvent implements Serializable {

    List<Complaint> complaints;

    public ComplaintEvent(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}
