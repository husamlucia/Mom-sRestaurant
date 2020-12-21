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
    @JoinColumn(name="menu_id", referencedColumnName = "id")
    private Menu menu;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "branch")
    private Branch branch;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "image")
    private int[] Image;

    public Meal(String name, double price, Branch branch, String ingredients, int[] image) {
        this.name = name;
        this.price = price;
        this.branch = branch;
        this.ingredients = ingredients;
        Image = image;
    }
}
