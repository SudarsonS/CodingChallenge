package de.bonial.transportmanager.service;

import de.bonial.transportmanager.handlers.Handler;
import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Request;
import de.bonial.transportmanager.model.Summary;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class PassengerServiceImpl implements PassengerService {

    private Set<Handler> handlers;

    @Inject
    public PassengerServiceImpl(Set<Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public Map<Mode, Integer> compute(Request request) {
        ArrayList<Summary> summaries = new ArrayList<>();
        request.getTransports()
                .forEach(map -> handlers.stream()
                        .filter(h -> h.canHandle(map))
                        .map(h -> h.numberOfPassengers(map))
                        .collect(Collectors.toCollection(() -> summaries)));

        return summaries.stream()
                .collect(groupingBy(Summary::getMode, summingInt(Summary::getCount)));
    }
}
