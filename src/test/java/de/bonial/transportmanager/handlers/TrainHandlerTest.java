package de.bonial.transportmanager.handlers;

import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Summary;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class TrainHandlerTest {

    @Test
    public void shouldReturnTrueWhenThereIsWPassengerCapacityKey() {
        TrainHandler trainHandler = new TrainHandler();
        Map map = of("model", "ICE", "number-wagons", 3, "w-passenger-capacity", 4);

        boolean canHandle = trainHandler.canHandle(map);

        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldReturnWhenFalseThereIsNoWPassengerCapacityKey() {
        TrainHandler trainHandler = new TrainHandler();
        Map map = of("model", "Boeing 777", "b-passenger-capacity", 12, "e-passenger-capacity", 4);

        boolean canHandle = trainHandler.canHandle(map);

        assertThat(canHandle).isFalse();
    }

    @Test
    public void shouldReturnProductOfWagonsWithPassengerPerWagon() {
        TrainHandler trainHandler = new TrainHandler();
        Map map = of("model", "ICE", "number-wagons", 3, "w-passenger-capacity", 4);

        Summary numberOfPassengers = trainHandler.numberOfPassengers(map);

        assertThat(numberOfPassengers.getMode()).isEqualTo(Mode.TRAIN);
        assertThat(numberOfPassengers.getCount()).isEqualTo(12);
    }
}