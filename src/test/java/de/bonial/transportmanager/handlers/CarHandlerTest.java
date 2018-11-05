package de.bonial.transportmanager.handlers;

import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Summary;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

public class CarHandlerTest {

    @Test
    public void shouldReturnTrueWhenThereIsPassengerCapacityKey() {
        CarHandler carHandler = new CarHandler();
        Map map = of("model", "M3", "manufacturer", "BMW", "passenger-capacity", 4);

        boolean canHandle = carHandler.canHandle(map);

        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldReturnWhenFalseThereIsNoPassengerCapacityKey() {
        CarHandler carHandler = new CarHandler();
        Map map = of("model", "Boeing 777", "b-passenger-capacity", 12, "e-passenger-capacity", 4);

        boolean canHandle = carHandler.canHandle(map);

        assertThat(canHandle).isFalse();
    }

    @Test
    public void shouldReturn4WhenThereIsPassengerCapacityIs4() {
        CarHandler carHandler = new CarHandler();
        Map map = of("model", "M3", "manufacturer", "BMW", "passenger-capacity", 4);

        Summary numberOfPassengers = carHandler.numberOfPassengers(map);

        assertThat(numberOfPassengers.getMode()).isEqualTo(Mode.CAR);
        assertThat(numberOfPassengers.getCount()).isEqualTo(4);
    }

}