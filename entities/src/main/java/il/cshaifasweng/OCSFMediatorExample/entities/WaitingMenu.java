package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WaitingMenus")
public class WaitingMenu implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private int id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="menu")
    private List<Meal> meals;

    public WaitingMenu(){
        this.meals = new ArrayList<>();

    }

    public void addMeal(Meal meal){
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


