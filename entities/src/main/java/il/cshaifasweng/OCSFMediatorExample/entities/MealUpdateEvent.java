package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.MealUpdate;

import java.io.Serializable;
import java.util.List;

public class MealUpdateEvent implements Serializable {

    List<MealUpdate> mealUpdates;

    public MealUpdateEvent(List<MealUpdate> mealUpdates) {
        this.mealUpdates = mealUpdates;
    }

    public List<MealUpdate> getMealUpdates() {
        return mealUpdates;
    }

    public void setMealUpdates(List<MealUpdate> mealUpdates) {
        this.mealUpdates = mealUpdates;
    }
}
