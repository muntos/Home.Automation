package home.network.automation.jobs;

import home.network.automation.devices.generic.NetworkAudioDevice;
import home.network.automation.devices.philips.HueBridge;
import home.network.automation.devices.api.OpenWeatherMap;
import home.network.automation.model.ForecastWeather;
import home.network.automation.model.philipsHue.HueLightState;
import home.network.automation.model.philipsHue.HueMotionSensor;
import home.network.automation.model.philipsHue.HueSensor;
import home.network.automation.observer.House;
import home.network.automation.service.CommandsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Profile({"prod", "dev"})
@Slf4j
@Component
public class NotifyOfBadWeatherJob {
    @Value("${house.location}")
    private String location;

    @Autowired
    private OpenWeatherMap openWeatherMap;

    @Autowired
    private House house;

    @Autowired
    private CommandsService commandsService;

    private HueBridge bridge;

    @Autowired
    public NotifyOfBadWeatherJob(House house) {
        this.house = house;
        bridge = house.getDevice("hue");
    }

    @Scheduled(fixedDelay = 5000)
    private void checkHallwaySensor() {
        HueSensor sensor = bridge.getSensor("Hallway sensor", HueMotionSensor.class);

        if (((HueMotionSensor) sensor).getState().getPresence()) {
            int numberOfDaysInFuture = 0;
            Optional<ForecastWeather> forecastWithRain = openWeatherMap.isRainInForecast(location, numberOfDaysInFuture);
            if (forecastWithRain.isPresent()) {
                log.info("{} ({}) in forecast for today in {} at {} o'clock!",
                        forecastWithRain.get().getMain(), forecastWithRain.get().getDescription(), location, forecastWithRain.get().getForecastedTime().getHourOfDay());
                flashHallwayStrip();
            }
        }
    }

    //@Scheduled(fixedDelay = 5000)
    private void test() {
        NetworkAudioDevice networkAudioDevice = house.getDevice("X4500H");
        commandsService.telnetConnect(networkAudioDevice);
        int x = 1;

    }

    private void flashHallwayStrip() {
        HueLightState onState = new HueLightState();
        onState.setOn(true);
        onState.setBrightness(80);
        onState.setXy(new Float[] {0.675f,0.322f});
        onState.setAlert("lselect");
        bridge.setLight(9, onState);
    }

}
