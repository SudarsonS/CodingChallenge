package de.bonial.transportmanager.handlers;

import com.google.common.collect.ImmutableMap;
import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Summary;

import java.util.Map;

public class FlightHandler implements Handler {

    @Override
    public boolean canHandle(Map map) {
        return map.containsKey("b-passenger-capacity") ||
                map.containsKey("e-passenger-capacity");
    }

    @Override
    public Summary numberOfPassengers(Map map) {
        return new Summary(Mode.FLIGHT, (Integer) map.getOrDefault("b-passenger-capacity", 0) +
                (Integer) map.getOrDefault("e-passenger-capacity", 0));
    }
}
