package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.management.loading.PrivateClassLoader;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private int id;

    @OneToMany(mappedBy="menu")
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
