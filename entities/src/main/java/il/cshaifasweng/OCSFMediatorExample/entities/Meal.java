package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Table;
import java.util.*;

import javax.persistence.*;


@Entity
@Table(name = "meal")// create table of meal in database
public class Meal  {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    @Column(name = "Price")
    private int price;
    @Column(name = "Ingredients")
    private List<String> ingredients;
    @Column(name = "Branch")
    private Branch branch;
    @Column(name = "Image")
    private int[] Image;


    public Meal(){}

    public Meal(int price, List<String> ingredient, Branch branch, int[] Im) {
        this.price = price;
        this.ingredients = ingredient;
        this.branch = branch;
        this.Image = Im;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getPrice() {
        return price;
    }

    public int[] getImage() {
        return Image;
    }

    public void setImage(int[] image) {
        Image = image;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
