package de.bonial.transportmanager.service;

import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Request;

import java.util.Map;

public interface PassengerService {

    Map<Mode, Integer> compute(Request request);
}
