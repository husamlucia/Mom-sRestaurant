package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class LoginMessage implements Serializable {

    private String type, identifier1, identifier2;

    public LoginMessage(String type, String identifier1, String identifier2) {
        this.type = type;
        this.identifier1 = identifier1;
        this.identifier2 = identifier2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier1() {
        return identifier1;
    }

    public void setIdentifier1(String identifier1) {
        this.identifier1 = identifier1;
    }

    public String getIdentifier2() {
        return identifier2;
    }

    public void setIdentifier2(String identifier2) {
        this.identifier2 = identifier2;
    }
}
