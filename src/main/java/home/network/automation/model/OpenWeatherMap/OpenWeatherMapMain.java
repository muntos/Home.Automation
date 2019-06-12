package home.network.automation.model.OpenWeatherMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenWeatherMapMain {
    private Double temp;
    private Integer pressure;
    private Integer humidity;
}
