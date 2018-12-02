package home.network.automation.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import home.network.automation.entity.EnvironmentReading;
import home.network.automation.model.Location;
import home.network.automation.model.Period;
import home.network.automation.model.Sensor;
import home.network.automation.repository.EnvironmentReadingRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EnvironmentReadingServiceImpl implements EnvironmentReadingService {
    @Autowired
    private EnvironmentReadingRepository repository;

    @Override
    public String getReadings(Location location, Sensor sensor, String sPeriod) {
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

        List<EnvironmentReading> readings = repository.findAllByLocationAndSensorAndDateAfterOrderByDateAsc(location, sensor, startDate);

        String jsonString = gson.toJson(readings);

        return jsonString;
    }
}
