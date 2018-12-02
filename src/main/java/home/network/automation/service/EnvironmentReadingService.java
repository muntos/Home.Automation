package home.network.automation.service;

import home.network.automation.model.Location;
import home.network.automation.model.Sensor;

public interface EnvironmentReadingService {
    String getReadings(Location location, Sensor sensor, String period);
}
