package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="mealUpdates")
public class MealUpdate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
            @JoinColumn(name="oldMeal_id",referencedColumnName = "id")
    Meal oldMeal;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="newMeal_id",referencedColumnName = "id")
    Meal newMeal;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    Branch branch;


    int newBranchId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getNewBranchId() {
        return newBranchId;
    }

    public void setNewBranchId(int newBranchId) {
        this.newBranchId = newBranchId;
    }

    String status="";

    public MealUpdate(){

    }
    public MealUpdate(Meal oldMeal, Meal newMeal, Branch branch, int newBranchId) {
        this.oldMeal = oldMeal;
        this.newMeal = newMeal;
        this.status = "Awaiting";
        this.branch = branch;
        this.newBranchId = newBranchId;
    }

    public Branch getBr() {
        return branch;
    }

    public void setBr(Branch br) {
        this.branch = br;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Meal getOldMeal() {
        return oldMeal;
    }

    public void setOldMeal(Meal oldMeal) {
        this.oldMeal = oldMeal;
    }

    public Meal getNewMeal() {
        return newMeal;
    }

    public void setNewMeal(Meal newMeal) {
        this.newMeal = newMeal;
    }
}
