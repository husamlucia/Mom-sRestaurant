package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.CollectionId;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.persistence.Table;


@Entity
@Table(name = "meals")// create table of meal in database
public class Meal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;


    @ManyToOne
    @JoinColumn(name="menu_id", referencedColumnName = "menu_id")
    private Menu menu;

    private int status; //0 edit, 1 add, 2 remove,3 upDated

    @ManyToOne
    @JoinColumn(name="WaitingMenu_id", referencedColumnName = "WaitingMenu_id")
    private WaitingMenu WaitingMenu;


     @ManyToMany(mappedBy ="meals")
    List<Order> orders;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ingredients;


    public Meal(){

    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public WaitingMenu getWaitingMenu() {
        return WaitingMenu;
    }

    public void setWaitingMenu(WaitingMenu waitingMenu) {
        WaitingMenu = waitingMenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


//    public void addOrder(Order order){
//        this.orders.add(order);
//    }

    public Meal(String name, double price, List<String> ingredients,int status) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.status=status;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
