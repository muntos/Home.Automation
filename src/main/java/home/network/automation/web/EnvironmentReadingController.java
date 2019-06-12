package home.network.automation.web;

import home.network.automation.model.Aggregate;
import home.network.automation.model.Location;
import home.network.automation.model.Sensor;
import home.network.automation.service.EnvironmentReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reading")
public class EnvironmentReadingController {
    @Autowired
    private EnvironmentReadingService readingService;


    @RequestMapping(value = "/balcony/temp", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public String getLivingBalconyTemperatureReadings(@RequestParam(value = "period", required = false) String period, @RequestParam(value = "minMax", required = false) String aggregate){
        return readingService.getReadings(Location.BALCONY_LIVING, Sensor.TEMPERATURE, period, Aggregate.of(aggregate));
    }

    @RequestMapping(value = "/balcony/humidity", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public String getLivingBalconyHumidityReadings(@RequestParam(value = "period", required = false) String period, @RequestParam(value = "minMax", required = false) String aggregate){
        return readingService.getReadings(Location.BALCONY_LIVING, Sensor.HUMIDITY, period, Aggregate.of(aggregate));
    }

    @RequestMapping(value = "/outside/temp", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public String getOutsideTemperatureReadings(@RequestParam(value = "period", required = false) String period, @RequestParam(value = "minMax", required = false) String aggregate){
        return readingService.getReadings(Location.OUTSIDE, Sensor.TEMPERATURE, period, Aggregate.of(aggregate));
    }
}