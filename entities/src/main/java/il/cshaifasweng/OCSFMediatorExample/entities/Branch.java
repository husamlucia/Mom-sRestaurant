package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "branches")
public class Branch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "openh")
    private String openHours;

    @Column(name = "closeh")
    private String closeHours;


    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="menu_id")
    private Menu menu;

    public WaitingMenu getWaitingMenu() {
        return waitingMenu;
    }

    public void setWaitingMenu(WaitingMenu waitingMenu) {
        this.waitingMenu = waitingMenu;
    }

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="WaitingMenu_id")
    private WaitingMenu waitingMenu;



    @OneToMany(mappedBy = "customerDetails",cascade=CascadeType.ALL)
    List<Order> orders;

    @OneToMany(mappedBy = "branch",cascade=CascadeType.ALL)
    List<Mapp> map;

    public Mapp getMap(String area){

        if(area.equals("inside")) return map.get(0);
        else return map.get(1);

    }


    public List<Booking> book(String date, String time, String area, int persons) throws ParseException {
        Mapp map = this.getMap(area);
        if(map != null ){
            return map.getPossibleBookings(date, time, persons);
        }
        return null;
    }

    public void setMap(String area, Mapp map2){
       map.add(map2);
    }

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
        this.map = new ArrayList<>();
    }

    public Branch(){

    }
}
