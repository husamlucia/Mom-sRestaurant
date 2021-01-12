package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="orders")
public class Order  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="order_meals",
            joinColumns = {@JoinColumn(name="order_id")},
            inverseJoinColumns = { @JoinColumn(name="meal_id")})
    private List<Meal> meals;

    private double price;

    private String different;
    private String pickup;

    @ManyToOne
    @JoinColumn(name="customer_id")
    CustomerDetails customerDetails;

    private String date;


    private String address;
    private String Recipient;
    private String recipientPhone;


    public Order(){}

    public Order(List<Meal> meals, String pickup, String different,CustomerDetails customerDetails, String recipientName,String recipientPhone, String address, double price) {
        this.meals = meals;
        this.date = date;
        this.different = different;
        this.pickup = pickup;
        this.address = address;
        this.Recipient = recipientName;
        this.recipientPhone = recipientPhone;
        this.customerDetails = customerDetails;
        this.price = price;
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

    public String isDifferent() {
        return different;
    }

    public void setDifferent(String different) {
        this.different = different;
    }

    public String isPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDifferent() {
        return different;
    }

    public String getPickup() {
        return pickup;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }
}
