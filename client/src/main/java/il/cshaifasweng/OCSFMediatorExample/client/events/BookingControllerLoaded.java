package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;

public class BookingControllerLoaded {
    private Branch branch;

    boolean inside, outside;
    public BookingControllerLoaded(Branch branch) {
        this.branch = branch;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
