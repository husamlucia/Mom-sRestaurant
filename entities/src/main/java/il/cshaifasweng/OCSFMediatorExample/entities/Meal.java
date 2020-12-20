package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.CollectionId;
import java.util.*;

import javax.persistence.*;


@Entity
@Table(name = "meal")// create table of meal in database
public class Meal{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
