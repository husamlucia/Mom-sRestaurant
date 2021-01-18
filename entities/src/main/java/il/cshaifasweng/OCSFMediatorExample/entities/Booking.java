package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bookings")// create table of bookings in database
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Booking_id")
    private int id;

    private String status;


    @Column(name = "Date")
    private String bookingDate;

    @Column(name = "Time")
    private String time;

    @Column(name = "Area")
    private String area;

    @Column(name = "CustomersNum")
    private int customerNum;

    @ManyToOne
    @JoinColumn(name="branch_id")
    private Branch branch;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="booking_tables",
            joinColumns = {@JoinColumn(name="booking_id")},
            inverseJoinColumns = {@JoinColumn(name="table_id")})
    private List<Tablee> tables;


    public Booking(String bookingDate, String time, String area, int customerNum, Branch branch, List<Tablee> tables) {
        this.bookingDate = bookingDate;
        this.time = time;
        this.area = area;
        this.customerNum = customerNum;
        this.branch = branch;
        this.tables = tables;
        this.status = "Active";
    }

    public Booking(){

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDate() {
        return bookingDate;
    }

    public void setDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(int customerNum) {
        this.customerNum = customerNum;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Tablee> getTables() {
        return tables;
    }

    public void setTables(List<Tablee> tables) {
        this.tables = tables;
    }
}
