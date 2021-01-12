package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "branches")
public class Branch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="menu_id")
    private Menu menu;

//    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
//    @JoinColumn(name = "Booking_id")
//    private List<Booking> bookingsList;         //table of branch bookings

    @Column(name = "openh")
    private String openHours;

    @Column(name = "closeh")
    private String closeHours;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getCloseHours() {
        return closeHours;
    }

    public void setCloseHours(String closeHours) {
        this.closeHours = closeHours;
    }

    public Branch(String openHours, String closeHours) {
        this.openHours = openHours;
        this.closeHours = closeHours;
        this.menu = new Menu();
    }

    public Branch(){

    }
}
