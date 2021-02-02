package il.cshaifasweng.OCSFMediatorExample.client.events;

import java.io.Serializable;

public class ConnectionEvent implements Serializable {

    String host;
    int port;

    public ConnectionEvent(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
