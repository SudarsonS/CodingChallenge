package de.bonial.transportmanager.handlers;

import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Summary;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

public class FlightHandlerTest {

    @Test
    public void shouldReturnFalseWhenThereIsNoEAndBPassengerCapacityKey() {
        FlightHandler flightHandler = new FlightHandler();
        Map map = of("model", "M3", "manufacturer", "BMW", "passenger-capacity", 4);

        boolean canHandle = flightHandler.canHandle(map);

        assertThat(canHandle).isFalse();
    }

    @Test
    public void shouldReturnWhenTrueThereIsEPassengerCapacityKey() {
        FlightHandler flightHandler = new FlightHandler();
        Map map = of("model", "Boeing 777", "e-passenger-capacity", 4);

        boolean canHandle = flightHandler.canHandle(map);

        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldReturnWhenTrueThereIsBPassengerCapacityKey() {
        FlightHandler flightHandler = new FlightHandler();
        Map map = of("model", "Boeing 777", "b-passenger-capacity", 12);

        boolean canHandle = flightHandler.canHandle(map);

        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldReturnWhenTrueThereIsEAndBPassengerCapacityKey() {
        FlightHandler flightHandler = new FlightHandler();
        Map map = of("model", "Boeing 777", "b-passenger-capacity", 12, "e-passenger-capacity", 4);

        boolean canHandle = flightHandler.canHandle(map);

        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldReturnSumOfEAndBPassengers() {
        FlightHandler flightHandler = new FlightHandler();
        Map map = of("model", "Boeing 777", "b-passenger-capacity", 12, "e-passenger-capacity", 4);

        Summary numberOfPassengers = flightHandler.numberOfPassengers(map);

        assertThat(numberOfPassengers.getMode()).isEqualTo(Mode.FLIGHT);
        assertThat(numberOfPassengers.getCount()).isEqualTo(16);
    }


}