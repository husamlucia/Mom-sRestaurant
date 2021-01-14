package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
@Entity
@Table(name = "tables")
public class Tablee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int capacity;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "map_id")
    private Mapp map;


    @ManyToMany(mappedBy = "tables",cascade=CascadeType.ALL)
    List<Booking> bookings;



    public Tablee(int capacity, Mapp map) {
        this.capacity = capacity;
        this.map = map;
        this.bookings = new ArrayList<>();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void removeBooking(Booking booking){
        bookings.remove(booking);
    }

    //hour = 14:00
    //can't have reservation from 12:15 to 15:59


    public boolean compare_date_for_booking(Date newHour, Date existingHour){
        Calendar upperBound = Calendar.getInstance();
        upperBound.setTime(newHour);
        upperBound.add(Calendar.HOUR_OF_DAY, 1);
        upperBound.add(Calendar.MINUTE, 59);

        Calendar lowerBound = Calendar.getInstance();
        lowerBound.setTime(newHour);
        lowerBound.add(Calendar.HOUR_OF_DAY, -1);
        lowerBound.add(Calendar.HOUR_OF_DAY, -59);

        Calendar existingHourCal = Calendar.getInstance();
        existingHourCal.setTime(existingHour);

        if ( existingHourCal.before(lowerBound) && existingHourCal.after(upperBound)) return true;
        return false;
    }

    public int checkAvailable(String date, String hour) throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Date hour2 = parser.parse(hour);


        System.out.println("Checking availability for: " + date + ' ' + hour);
        for(Booking book: bookings){
            if(book.getDate().equals(date)){
                Date bookHour = parser.parse(book.getTime());
                if(!compare_date_for_booking(hour2, bookHour)) return 0;
            }
        }
        return this.getCapacity();
    }

    public boolean checkTimeIntersect(String hour, String bookingHour){
        LocalTime first = LocalTime.parse(hour);
        LocalTime second = LocalTime.parse(bookingHour);
        Duration duration = Duration.between( first, second );
        long diff = duration.getSeconds()/60;
        boolean intersect = (diff<120 && diff > -120);
        return intersect;
    }

    public boolean isAvailable(String date, String hour){
        for(Booking booking: bookings){
            String bookingDate = booking.getDate();
            if(!bookingDate.equals(date)) continue;
            String bookingHour = booking.getTime();
            boolean intersect = checkTimeIntersect(hour, bookingHour);
            if (intersect == true) return false;
        }
        return true;
    }


    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Mapp getMap() {
        return map;
    }

    public Tablee(){

    }

    public void setMap(Mapp map) {
        this.map = map;
    }

}
