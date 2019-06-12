package home.network.automation.model.OpenWeatherMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenWeatherMapNowResponse {
    private Long id;
    private String name;
    private OpenWeatherMapMain main;
}
