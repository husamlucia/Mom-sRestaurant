package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.CollectionId;
import java.util.*;

import javax.persistence.*;


@Entity
@Table(name = "meals")// create table of meal in database
public class Meal{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="menu_id", referencedColumnName = "menu_id")
    private Menu menu;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @ElementCollection
    @CollectionTable(name= "ingredients")
    private List<String> ingredients;

    @Column(name = "image")
    private int[] Image;

    public Meal(String name, double price, List<String> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
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

    public int[] getImage() {
        return Image;
    }

    public void setImage(int[] image) {
        Image = image;
    }
}
