package il.cshaifasweng.OCSFMediatorExample.entities;

import org.dom4j.Branch;
import org.hibernate.annotations.Table;

import javax.management.loading.PrivateClassLoader;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.*;

@Entity
@Table(name = "Menu")
public class Menu {

    @Column(name = "meals")
    private List<Meal> Meals;


    Menu(List<Meal> meals){
        this.Meals = meals;
    }



}
