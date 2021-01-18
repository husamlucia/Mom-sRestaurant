package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="order_meals",
            joinColumns = {@JoinColumn(name="order_id")},
            inverseJoinColumns = { @JoinColumn(name="meal_id")})
    private List<Meal> meals;

    @ManyToOne
    private Branch br;

    private double price;

    private boolean different;
    private boolean pickup;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id")
    CustomerDetails customerDetails;

    private String date;
    private String hour;

    private String address;
    private String Recipient;
    private String recipientPhone;

    private String status;

    public Order(){}

    public Order(Branch br, List<Meal> meals, boolean pickup, boolean different,CustomerDetails customerDetails, String recipientName,String recipientPhone, String address, double price, String date,  String hour) {
        this.br = br;
        this.meals = meals;
        this.date = date;
        this.different = different;
        this.pickup = pickup;
        this.address = address;
        this.Recipient = recipientName;
        this.recipientPhone = recipientPhone;
        this.customerDetails = customerDetails;
        this.price = price;
        this.status = "Active";
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }


    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public Branch getBr() {
        return br;
    }

    public void setBr(Branch br) {
        this.br = br;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isDifferent() {
        return different;
    }

    public void setDifferent(boolean different) {
        this.different = different;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }
}
