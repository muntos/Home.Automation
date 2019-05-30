package home.network.automation.model.OpenWeatherMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenWeatherMapWeather {
    private Integer id;
    private String main;
    private String description;
    private String icon;
}
