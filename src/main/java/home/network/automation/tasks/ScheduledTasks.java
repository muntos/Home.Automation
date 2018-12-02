package home.network.automation.tasks;

import home.network.automation.devices.HarmonyHub;
import home.network.automation.devices.api.OpenWeatherMap;
import home.network.automation.devices.broadlink.A1Sensor;
import home.network.automation.entity.EnvironmentReading;
import home.network.automation.model.A1SensorResponse;
import home.network.automation.model.Location;
import home.network.automation.model.OpenWeatherMapNowResponse;
import home.network.automation.model.Sensor;
import home.network.automation.observer.House;
import home.network.automation.repository.EnvironmentReadingRepository;
import lombok.extern.slf4j.Slf4j;
import net.whistlingfish.harmony.HarmonyClient;
import net.whistlingfish.harmony.config.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;


@Component
@Slf4j
public class ScheduledTasks {
    private HarmonyClient harmonyClient;

    @Autowired
    private House house;

    @Autowired
    private EnvironmentReadingRepository environmentReadingRepository;

    @Autowired
    private OpenWeatherMap openWeatherMap;

    @Scheduled(fixedDelay = 30000)
    public void checkHarmonyStatus() {
        HarmonyHub harmonyHub = house.getDevice("Harmony");
        harmonyClient = harmonyHub.getHarmonyClient();
        try {
            Activity currentActivity = harmonyClient.getCurrentActivity();
        }catch (RuntimeException ex){
            log.error("Error when trying to check Harmony Hub status: {}", ex.getMessage());
            log.info("Trying to reconnect to Harmony Hub {}", harmonyHub.getAddress());
            try{
                harmonyClient.connect(harmonyHub.getAddress());
            } catch (RuntimeException e){
                log.warn("Failed connecting to Harmony Hub {} error is: {}", harmonyHub.getAddress(), e.getMessage());
            }
        }
    }

    @Scheduled(fixedDelay = 3600000)
    public void saveEnvironmentReadings(){
        A1Sensor a1Sensor = house.getDevice("A1_Balcony_Living");
        Date now = new Date();

        Optional<A1SensorResponse> a1SensorResponse = a1Sensor.getReadings();

        if (a1SensorResponse.isPresent()) {
            Optional<OpenWeatherMapNowResponse> currentWeather = openWeatherMap.getCurrentWeather("Bucharest");
            if (currentWeather.isPresent()) {
                saveReadingValue(Location.BALCONY_LIVING, Sensor.TEMPERATURE, a1SensorResponse.get().getTemperature(), now);
                saveReadingValue(Location.BALCONY_LIVING, Sensor.HUMIDITY, a1SensorResponse.get().getHumidity(), now);
                saveReadingValue(Location.OUTSIDE, Sensor.TEMPERATURE, currentWeather.get().getMain().getTemp(), now);
                saveReadingValue(Location.OUTSIDE, Sensor.HUMIDITY, Double.valueOf(currentWeather.get().getMain().getHumidity()), now);
            } else {
                log.warn("Failed to get current weather !");
            }
        } else {
            log.warn("Error reading values from A1 sensor in Living Balcony");
        }

    }

    private void saveReadingValue(Location location, Sensor sensor, Double value, Date date){
        EnvironmentReading environmentReading = new EnvironmentReading();
        environmentReading.setDate(date);
        environmentReading.setLocation(location);
        environmentReading.setSensor(sensor);
        environmentReading.setValue(value);

        log.info("Saving reading: {}", environmentReading);
        environmentReadingRepository.save(environmentReading);
    }

}