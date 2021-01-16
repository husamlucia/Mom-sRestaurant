package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name = "complaints")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private String date;

    @Column(name = "complaint")
    private String complaint;

    @ManyToOne
    @JoinColumn(name="customer_id")
    CustomerDetails customerDetails;

    public Complaint(String date, String complaint,CustomerDetails customerDetails) {
        this.date = date;
        this.complaint = complaint;
        this.customerDetails=customerDetails;

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
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }



}
