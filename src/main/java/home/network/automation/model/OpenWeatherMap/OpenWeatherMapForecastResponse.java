package home.network.automation.model.OpenWeatherMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenWeatherMapForecastResponse {
    private List<OpenWeatherMapForecastList> list;
}
