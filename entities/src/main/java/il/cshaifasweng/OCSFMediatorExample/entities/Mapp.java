package il.cshaifasweng.OCSFMediatorExample.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "maps")
public class Mapp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL)
    private List<Tablee> tables;

    private String area;

    private int maxCapacity;


    public Mapp(Branch br, String area) {
        this.branch = br;
        this.tables = new ArrayList<>();
        this.area = area;
        this.maxCapacity = 0;
        br.setMap(area, this);
    }

    public Mapp() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void addTable(Tablee table) {
        this.tables.add(table);
        maxCapacity += table.getCapacity();
    }


    public List<Tablee> getTables() {
        return tables;
    }

    public void setTables(List<Tablee> tables) {
        this.tables = tables;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }


    public List<Booking> getPossibleBookings(String date, String hour, int persons, int maxPurple) throws ParseException {
        List<Booking> availableBookings = new ArrayList<>();
        LocalTime currHour = LocalTime.parse(hour);
        LocalTime closeHour = LocalTime.parse(this.branch.getCloseHours());
        Booking booking = tryBooking(date, hour, persons, maxPurple);
        if (booking != null) {
            availableBookings.add(booking);
            return availableBookings;
        }
        while (Duration.between(currHour, closeHour).getSeconds() / 60 < 0) {
            System.out.println(Duration.between(currHour, closeHour).getSeconds() / 60);
            currHour = currHour.plusMinutes(15);
            String currHourString = currHour.toString();
            System.out.println("currhourString = " + currHourString);
            booking = tryBooking(date, currHourString, persons, maxPurple);
            availableBookings.add(booking);
            if (booking != null) {
                availableBookings.add(booking);
            }
        }
        return availableBookings;
    }

    public int getNumCustomersSitting(String date, String hour) {
        int customers = 0;
        for (Tablee table : tables) {
            if (!table.isAvailable(date, hour)) {
                int sitting = table.getSitting(date, hour);
                System.out.println(sitting);
                customers += table.getSitting(date, hour);
            }
        }
        return customers;
    }

    public Booking tryBooking(String date, String hour, int persons, int maxPurple) {
        int allowedToSit = maxCapacity;
        int countSitting = getNumCustomersSitting(date, hour);
        if(maxPurple != -1){
           allowedToSit = Math.min(allowedToSit, maxPurple);
        }

        if(countSitting > allowedToSit) return null;

        List<Booking> availableBookings = new ArrayList<>();
        tables.sort(Comparator.comparing(Tablee::getCapacity).reversed());
        List<Tablee> freeTables = new ArrayList<>();
        int countAvailableSeats = 0;
        for (Tablee table : tables) {
            if (table.isAvailable(date, hour)) {
                freeTables.add(table);
                countAvailableSeats += table.getCapacity();
                if (countAvailableSeats >= persons) {
                    Booking booking = new Booking(date, hour, area, persons, branch, freeTables);
                    return booking;
                }
            }
        }
        return null;
    }

    public OccupationMap getOccupationMap(String date, String hour) {
        OccupationMap map = new OccupationMap();
        SimpleTable simpleTable;
        for (Tablee table : tables) {
            if (table.isAvailable(date, hour)) {
                simpleTable = new SimpleTable(table.getId(), table.getCapacity(), "Available");
            } else {
                simpleTable = new SimpleTable(table.getId(), table.getCapacity(), "Occupied");
            }
            map.addTable(simpleTable);
        }
        return map;
    }

    public void cancelBookings(LocalDate qStart, LocalDate qEnd){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        for(Tablee table: tables){
            List<Booking> bookings = table.getBookings();
            for(Booking booking: bookings){
                LocalDate bookingDate = LocalDate.parse(booking.getDate(), formatter);
                if(bookingDate.isAfter(qStart) && bookingDate.isBefore(qEnd)){
                    booking.setStatus("CancelledByQuarantine");
                }
            }
        }
    }

}
