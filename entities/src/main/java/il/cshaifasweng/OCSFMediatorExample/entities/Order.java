package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="orders")
public class Order  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<Meal> meals;

    private LocalDate date;

    private String address;

    private String Recipient;

    private String phoneNumber;

    private String customerName;

    private String recipientPhone;

    private String different;

    private String pickup;

    private String creditCard;



    public Order(){}

    public Order(List<Meal> meals, String pickup, String different,String customerName, String phoneNumber, String creditCard,String recipientName,String recipientPhone, String address) {
        this.meals = meals;
        this.date = date;
        this.different = different;
        this.pickup = pickup;
        this.address = address;
        this.Recipient = recipientName;
        this.phoneNumber = phoneNumber;
        this.customerName = customerName;
        this.recipientPhone = recipientPhone;
        this.creditCard = creditCard;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }
}
