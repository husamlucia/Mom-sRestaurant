package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class ReportRequest implements Serializable {


    int id, month;

    boolean orders, cancelledOrders, complaints;


    public ReportRequest(int id, int month, boolean orders, boolean cancelledOrders, boolean complaints) {
        this.id = id;
        this.month = month;
        this.orders = orders;
        this.cancelledOrders = cancelledOrders;
        this.complaints = complaints;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isOrders() {
        return orders;
    }

    public void setOrders(boolean orders) {
        this.orders = orders;
    }

    public boolean isCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(boolean cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public boolean isComplaints() {
        return complaints;
    }

    public void setComplaints(boolean complaints) {
        this.complaints = complaints;
    }
}
