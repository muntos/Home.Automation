package home.network.automation.service;

import home.network.automation.model.Aggregate;
import home.network.automation.model.Location;
import home.network.automation.model.Sensor;

import java.util.Optional;

public interface EnvironmentReadingService {
    String getReadings(Location location, Sensor sensor, String period, Optional<Aggregate> aggregate);
}
