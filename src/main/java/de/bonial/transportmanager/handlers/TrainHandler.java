package de.bonial.transportmanager.handlers;

import com.google.common.collect.ImmutableMap;
import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Summary;

import java.util.Map;

public class TrainHandler implements Handler {

    @Override
    public boolean canHandle(Map map) {
        return map.containsKey("w-passenger-capacity");
    }

    @Override
    public Summary numberOfPassengers(Map map) {
        return new Summary(Mode.TRAIN, (Integer) map.getOrDefault("w-passenger-capacity", 0) *
                (Integer) map.getOrDefault("number-wagons", 0));
    }
}
