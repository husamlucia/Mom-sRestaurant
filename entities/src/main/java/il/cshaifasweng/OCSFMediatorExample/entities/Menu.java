package il.cshaifasweng.OCSFMediatorExample.entities;

import org.dom4j.Branch;

import javax.management.loading.PrivateClassLoader;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToMany
    @Column(name = "meals")
    private List<Meal> meals;

    public Menu(){
        this.meals = new ArrayList<>();
    }

    void addMeal(Meal meal){
        meals.add(meal);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
