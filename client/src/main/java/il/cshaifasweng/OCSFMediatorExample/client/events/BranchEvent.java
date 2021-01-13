package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.BranchList;

public class BranchEvent {

    private BranchList branches;

    public BranchEvent(BranchList branches) {
        this.branches = branches;
    }

    public BranchList getBranches() {
        return branches;
    }

    public void setBranches(BranchList branches) {
        this.branches = branches;
    }
}
