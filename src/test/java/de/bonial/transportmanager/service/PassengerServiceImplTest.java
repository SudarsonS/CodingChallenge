package de.bonial.transportmanager.service;

import com.google.common.collect.ImmutableSet;
import de.bonial.transportmanager.handlers.CarHandler;
import de.bonial.transportmanager.handlers.FlightHandler;
import de.bonial.transportmanager.handlers.TrainHandler;
import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Request;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PassengerServiceImplTest {

    private PassengerService service;

    private CarHandler carHandler = new CarHandler();

    private TrainHandler trainHandler = new TrainHandler();

    private FlightHandler flightHandler = new FlightHandler();

    @Before
    public void setUp() throws Exception {
        service = new PassengerServiceImpl(ImmutableSet.of(carHandler, trainHandler, flightHandler));
    }

    @Test
    public void shouldReturnTheSummaryOfTransport() {
        Map boeing = of("model", "Boeing 777", "b-passenger-capacity", 12, "e-passenger-capacity", 4);
        Map bmw = of("model", "M3", "manufacturer", "BMW", "passenger-capacity", 4);
        Map benz = of("model", "E2", "manufacturer", "Benz", "passenger-capacity", 3);
        Map ice = of("model", "ICE", "number-wagons", 3, "w-passenger-capacity", 4);
        Request request = new Request(Arrays.asList(boeing, bmw, benz, ice));

        Map<Mode, Integer> summary = service.compute(request);

        assertThat(summary.get(Mode.CAR)).isEqualTo(7);
        assertThat(summary.get(Mode.TRAIN)).isEqualTo(12);
        assertThat(summary.get(Mode.FLIGHT)).isEqualTo(16);
    }
}