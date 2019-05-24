package home.network.automation.jobs;

import home.network.automation.devices.api.OpenWeatherMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile({"prod", "dev"})
@Slf4j
@Component
public class CheckRainForecastJob {

    @Autowired
    private OpenWeatherMap openWeatherMap;

    @Scheduled(fixedDelay = 30000)
    public void doJob() {
        int numberOfDaysInFuture = 1;
        Boolean isRain = openWeatherMap.isRainInForecast("Bucharest", numberOfDaysInFuture);
        log.info("Is rain in forecast for {} days in future: {}", numberOfDaysInFuture, isRain);
    }
}