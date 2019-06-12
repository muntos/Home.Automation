package home.network.automation.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import home.network.automation.entity.EnvironmentReading;
import home.network.automation.model.Aggregate;
import home.network.automation.model.Location;
import home.network.automation.model.Period;
import home.network.automation.model.Sensor;
import home.network.automation.repository.EnvironmentReadingRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class EnvironmentReadingServiceImpl implements EnvironmentReadingService {
    @Autowired
    private EnvironmentReadingRepository repository;

    @Override
    public String getReadings(Location location, Sensor sensor, String sPeriod, Optional<Aggregate> aggregate) {
        Period period = Period.of(sPeriod);
        Date startDate;
        Gson gson;

        switch (period){
            case TODAY:
                startDate = new DateTime().withTimeAtStartOfDay().toDate();
                gson = new GsonBuilder().setDateFormat("HH:mm").create();
                break;
            case WEEK:
                startDate = new DateTime().withTimeAtStartOfDay().dayOfWeek().withMinimumValue().toDate();
                gson = new GsonBuilder().setDateFormat("EEE HH:mm").create();
                break;
            case MONTH:
                startDate = new DateTime().withTimeAtStartOfDay().dayOfMonth().withMinimumValue().toDate();
                gson = new GsonBuilder().setDateFormat("dd-MMM HH:mm").create();
                break;
            case YEAR:
                startDate = new DateTime().withTimeAtStartOfDay().dayOfYear().withMinimumValue().toDate();
                gson = new GsonBuilder().setDateFormat("dd-MMM").create();
                break;
            case CUSTOM:
                startDate = new DateTime().withTimeAtStartOfDay().toDate();
                gson = new GsonBuilder().setDateFormat("HH:mm").create();
                break;
            default:
                startDate = new DateTime().withTimeAtStartOfDay().toDate();
                gson = new GsonBuilder().setDateFormat("HH:mm").create();
        }

        List<EnvironmentReading> readings;

        switch (period) {
            case TODAY:
                 readings = repository.findAllByLocationAndSensorAndDateAfterOrderByDateAsc(location, sensor, startDate);
                 break;
            case WEEK:
            case MONTH:
            case YEAR:
                if (aggregate.isPresent()) {
                    List<EnvironmentReading> readingsWithPossibleDuplicates = new ArrayList<>();
                    switch (aggregate.get()) {
                        case MIN:
                            readingsWithPossibleDuplicates = repository.findMinValueGroupByDate(location.toString(), sensor.toString(), startDate);
                            break;
                        case MAX:
                            readingsWithPossibleDuplicates = repository.findMaxValueGroupByDate(location.toString(), sensor.toString(), startDate);
                            break;
                    }

                    readings = readingsWithPossibleDuplicates
                            .stream()
                            .filter(distinctByKeys(EnvironmentReading::getValue, EnvironmentReading::getDateWithoutHour))
                            .collect(Collectors.toList());
                } else {
                    readings = repository.findAllByLocationAndSensorAndDateAfterOrderByDateAsc(location, sensor, startDate);
                }

                break;
            default:
                readings = repository.findAllByLocationAndSensorAndDateAfterOrderByDateAsc(location, sensor, startDate);

        }

        String jsonString = gson.toJson(readings);

        return jsonString;
    }

    private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors)
    {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }
}
