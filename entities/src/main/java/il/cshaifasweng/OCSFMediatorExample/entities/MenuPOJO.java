package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

public class MenuPOJO implements Serializable{

    private static final long serialVersionUID = -8224097662914849956L;

    public MenuPOJO() {
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    private List<Meal> meals;
}
