package de.bonial.transportmanager.handlers;

import de.bonial.transportmanager.model.Summary;

import java.util.Map;

public interface Handler {

    boolean canHandle(Map map);

    Summary numberOfPassengers(Map map);
}
