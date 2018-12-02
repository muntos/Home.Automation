package home.network.automation.repository;

import home.network.automation.entity.EnvironmentReading;
import home.network.automation.model.Location;
import home.network.automation.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EnvironmentReadingRepository extends JpaRepository<EnvironmentReading, Long> {
    List<EnvironmentReading> findAllByLocationAndSensorAndDateAfterOrderByDateAsc(Location location, Sensor sensor, Date date);
}
