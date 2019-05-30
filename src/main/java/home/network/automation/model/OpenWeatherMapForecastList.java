package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenWeatherMapForecastList {
    private Long dt;
    private OpenWeatherMapMain main;
    private List<OpenWeatherMapWeather> weather;
    @JsonProperty("dt_txt")
    private String dtString;
}
