package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="tables")
public class Tablee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    private Branch branch;

    private int capacity;
    Map<String, Booking> bookings;



    public void addBooking(Booking booking){
        String date = booking.getBookingDate();
        bookings.put(date, booking);
        }
}
