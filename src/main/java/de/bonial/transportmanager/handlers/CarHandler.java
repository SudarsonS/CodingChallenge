package de.bonial.transportmanager.handlers;

import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Summary;

import java.util.Map;

public class CarHandler implements Handler {

    @Override
    public boolean canHandle(Map map) {
        return map.containsKey("passenger-capacity");
    }

    @Override
    public Summary numberOfPassengers(Map map) {
        return new Summary(Mode.CAR, (Integer) map.get("passenger-capacity"));
    }
}
