package de.bonial.transportmanager.model;

public class Summary {

    private Mode mode;

    private Integer count;

    public Summary(Mode mode, Integer count) {
        this.mode = mode;
        this.count = count;
    }

    public Mode getMode() {
        return mode;
    }

    public Integer getCount() {
        return count;
    }
}
