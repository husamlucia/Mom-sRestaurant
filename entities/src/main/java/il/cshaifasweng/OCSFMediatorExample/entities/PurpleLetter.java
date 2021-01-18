package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="purpleLetter")
public class PurpleLetter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    private boolean quarantine;
    private boolean pickupAllowed;
    private boolean deliveryAllowed;
    private String quarantineStartDate;
    private String quarantineEndDate;
    private Integer maxInside, maxOutside;




    PurpleLetter() {
        this.quarantine = false;
        this.quarantineStartDate = "";
        this.quarantineEndDate = "";
        this.maxInside = -1;
        this.maxOutside = -1;
        this.pickupAllowed = true;
        this.deliveryAllowed = true;
    }


    public int getMax(String area){
        if (area.equals("inisde")) return maxInside;
        return maxOutside;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setPickupAllowed(boolean pickupAllowed) {
        this.pickupAllowed = pickupAllowed;
    }

    public boolean getPickupAllowed() {
        return pickupAllowed;
    }

    public boolean isPickupAllowed() {
        return pickupAllowed;
    }

    public boolean isDeliveryAllowed() {
        return deliveryAllowed;
    }

    public void setDeliveryAllowed(boolean deliveryAllowed) {
        this.deliveryAllowed = deliveryAllowed;
    }

    public void setMaxInside(Integer maxInside) {
        this.maxInside = maxInside;
    }

    public void setMaxOutside(Integer maxOutside) {
        this.maxOutside = maxOutside;
    }

    public boolean isQuarantine() {
        return quarantine;
    }

    public void setQuarantine(boolean quarantine) {
        this.quarantine = quarantine;
    }

    public String getQuarantineStartDate() {
        return quarantineStartDate;
    }

    public void setQuarantineStartDate(String quarantineStartDate) {
        this.quarantineStartDate = quarantineStartDate;
    }

    public String getQuarantineEndDate() {
        return quarantineEndDate;
    }

    public void setQuarantineEndDate(String quarantineEndDate) {
        this.quarantineEndDate = quarantineEndDate;
    }

    public int getMaxInside() {
        return maxInside;
    }

    public void setMaxInside(int maxInside) {
        this.maxInside = maxInside;
    }

    public int getMaxOutside() {
        return maxOutside;
    }

    public void setMaxOutside(int maxOutside) {
        this.maxOutside = maxOutside;
    }


    /*Responsible for updating:
     * 1. Maximum people that can sit inside/outside
     * 2. Days branch(es) are closed. Question is: Does quarantine  */
}