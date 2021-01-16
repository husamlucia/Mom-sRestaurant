package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.CollectionId;

import java.io.Serializable;
import java.util.*;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Table;


@Entity
@Table(name = "meals")// create table of meal in database
public class Meal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "menu_id")
    private Menu menu;

    @ManyToMany(mappedBy = "meals")
    List<Order> orders;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ingredients;


    @Lob
    private byte[] image;


    public Meal() {

    }

    public Meal(String name, double price, List<String> ingredients, byte[] image) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.image = image;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addOrder(Order order){
        this.orders.add(order);
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
