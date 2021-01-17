package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private String datetime;

    @Column(name = "complaint")
    private String complaint;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id")
    CustomerDetails customerDetails;

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="branch_id")
    Branch branch;

    public Complaint(String datetime, String complaint,CustomerDetails customerDetails,Branch branch) {
        this.datetime = datetime;
        this.complaint = complaint;
        this.customerDetails=customerDetails;
        this.branch=branch;

    }
    public Complaint() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return datetime;
    }

    public void setDate(String date) {
        this.datetime = date;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }
}
