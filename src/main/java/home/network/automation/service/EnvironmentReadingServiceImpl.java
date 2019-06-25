package home.network.automation.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import home.network.automation.entity.EnvironmentReading;
import home.network.automation.model.*;
import home.network.automation.repository.EnvironmentReadingRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
        String labelFormat;
        String dateFormat;

        switch (period){
            case WEEK:
                startDate = new DateTime().withTimeAtStartOfDay().dayOfWeek().withMinimumValue().toDate();
                gson = new GsonBuilder().setDateFormat("EEE HH:mm").create();
                labelFormat = "EEE";
                dateFormat = "'%d-%m-%Y'";
                break;
            case MONTH:
                startDate = new DateTime().withTimeAtStartOfDay().dayOfMonth().withMinimumValue().toDate();
                gson = new GsonBuilder().setDateFormat("dd-MMM HH:mm").create();
                labelFormat = "dd-MMM";
                dateFormat = "'%d-%m-%Y'";
                break;
            case YEAR:
                startDate = new DateTime().withTimeAtStartOfDay().dayOfYear().withMinimumValue().toDate();
                gson = new GsonBuilder().setDateFormat("dd-MMM").create();
                labelFormat = "MMM";
                dateFormat = "'%m-%Y'";
                break;
            default:
                startDate = new DateTime().withTimeAtStartOfDay().toDate();
                gson = new GsonBuilder().setDateFormat("HH:mm").create();
                labelFormat = "HH:mm";
                dateFormat = "'%d-%m-%Y'";
        }

        List<EnvironmentReading> readings;

        switch (period) {
            case WEEK:
            case MONTH:
            case YEAR:
                if (aggregate.isPresent()) {
                    List<EnvironmentReading> readingsWithPossibleDuplicates = new ArrayList<>();
                    switch (aggregate.get()) {
                        case MIN:
                            readingsWithPossibleDuplicates = repository.findMinValueGroupByDate(location.toString(), sensor.toString(), startDate, dateFormat);
                            break;
                        case MAX:
                            readingsWithPossibleDuplicates = repository.findMaxValueGroupByDate(location.toString(), sensor.toString(), startDate, dateFormat);
                            break;
                    }

                    if (period.equals(Period.MONTH)) {
                        readings = readingsWithPossibleDuplicates
                                .stream()
                                .filter(distinctByKeys(EnvironmentReading::getValue, EnvironmentReading::getDateWithoutHour))
                                .collect(Collectors.toList());
                    } else {
                        readings = readingsWithPossibleDuplicates
                                .stream()
                                .filter(distinctByKeys(EnvironmentReading::getValue, EnvironmentReading::getDateWithoutDay))
                                .collect(Collectors.toList());

                    }
                } else {
                    readings = repository.findAllByLocationAndSensorAndDateAfterOrderByDateAsc(location, sensor, startDate);
                }

                break;
            default:
                readings = repository.findAllByLocationAndSensorAndDateAfterOrderByDateAsc(location, sensor, startDate);

        }

        List<EnvironmentReadingRepresentation> result = readings.stream().map(temp -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(labelFormat);
            String label = simpleDateFormat.format(temp.getDate());
            EnvironmentReadingRepresentation representation = EnvironmentReadingRepresentation.builder().reading(temp).label(label).build();
            return representation;
        }).collect(Collectors.toList());

        String jsonString = gson.toJson(result);

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
