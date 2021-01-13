package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Worker;

public class LoginEvent {
    Worker worker;

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public LoginEvent(Worker worker) {
        this.worker = worker;
    }

}
