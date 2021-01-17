package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class SimpleTable implements Serializable {

    int id;
    String status;
    int capacity;

    public SimpleTable(int id, int capacity, String status ) {
        this.id = id;
        this.status = status;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
