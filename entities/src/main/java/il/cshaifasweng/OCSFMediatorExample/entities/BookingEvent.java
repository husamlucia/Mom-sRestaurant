package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.Booking;

import java.io.Serializable;
import java.util.List;

public class BookingEvent implements Serializable {

    private List<Booking> bookings;

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public BookingEvent(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
