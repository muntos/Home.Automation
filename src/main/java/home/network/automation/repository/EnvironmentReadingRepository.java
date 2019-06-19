package home.network.automation.repository;

import home.network.automation.entity.EnvironmentReading;
import home.network.automation.model.Location;
import home.network.automation.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EnvironmentReadingRepository extends JpaRepository<EnvironmentReading, Long> {
    List<EnvironmentReading> findAllByLocationAndSensorAndDateAfterOrderByDateAsc(Location location, Sensor sensor, Date date);

    @Query(
            value = "select E.* FROM environment_reading E\n" +
                    "join (\n" +
                    "\tselect DATE_FORMAT(date, :date_format) as day,\n" +
                    "min(value) as value\n" +
                    "    from environment_reading E\n" +
                    "    where E.location = :location and E.sensor = :sensor and E.date >=:date \n" +
                    "    group by DATE_FORMAT(date, :date_format)\n" +
                    "    ) X\n" +
                    "    ON DATE_FORMAT(E.date, :date_format) = X.day\n" +
                    "    AND E.value = X.value order by E.date;",
            nativeQuery = true
    )
    List<EnvironmentReading> findMinValueGroupByDate(@Param("location") String location, @Param("sensor") String sensor,@Param("date") Date date, @Param("date_format") String dateFormat);

    @Query(
            value = "select E.* FROM environment_reading E\n" +
                    "join (\n" +
                    "\tselect DATE_FORMAT(date, :date_format) as day,\n" +
                    "max(value) as value\n" +
                    "    from environment_reading E\n" +
                    "    where E.location = :location and E.sensor = :sensor and E.date >=:date \n" +
                    "    group by DATE_FORMAT(date, :date_format)\n" +
                    "    ) X\n" +
                    "    ON DATE_FORMAT(E.date, :date_format) = X.day\n" +
                    "    AND E.value = X.value order by E.date;",
            nativeQuery = true
    )
    List<EnvironmentReading> findMaxValueGroupByDate(@Param("location") String location, @Param("sensor") String sensor,@Param("date") Date date, @Param("date_format") String dateFormat);
}
