package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name = "bookings")// create table of bookings in database
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Booking_id")
    private int id;

    @Column(name = "Date")
    private String bookingDate;

    @Column(name = "Time")
    private  String time;

    @Column(name = "Area")
    private String area;

    @Column(name = "CustomersNum")
    private int customerNum;

    @OneToMany
    private Branch branch;


    public int getId() {
        return id;
    }

    public String getDate() {
        return bookingDate;
    }

    public String getTime() {
        return time;
    }

    public String getArea() {
        return area;
    }

    public int getCustomersNum() {
        return customerNum;
    }
//
//    public Branch getBranch() {
//        return Branch;
//    }

    public Booking(int id, String bookingDate, String time, String area, int customerNum) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.time = time;
        this.area = area;
        this.customerNum = customerNum;
    }
}
