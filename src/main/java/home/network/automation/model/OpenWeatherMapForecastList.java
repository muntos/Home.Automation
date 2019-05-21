package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenWeatherMapForecastList {
    private Long dt;
    private OpenWeatherMapMain main;
    private List<OpenWeatherMapWeather> weather;
}
