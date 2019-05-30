package home.network.automation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.time.DateTime;

@Getter
@AllArgsConstructor
public class ForecastWeather {
    private String main;
    private String description;
    private DateTime forecastedTime;
}
