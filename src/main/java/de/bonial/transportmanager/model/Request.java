package de.bonial.transportmanager.model;

import java.util.List;
import java.util.Map;

public class Request {

    private List<Map> transports;

    public Request() {
    }

    public Request(List<Map> transports) {
        this.transports = transports;
    }

    public List<Map> getTransports() {
        return transports;
    }

    public void setTransports(List<Map> transports) {
        this.transports = transports;
    }
}
