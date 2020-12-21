package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.management.loading.PrivateClassLoader;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @Column(name = "branch_id")
    private int id;

    @OneToMany(mappedBy="menu")
    private List<Meal> meals;

    @OneToOne
    @MapsId
    @JoinColumn(name="branch_id")
    private Branch branch;

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch= branch;
    }

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
