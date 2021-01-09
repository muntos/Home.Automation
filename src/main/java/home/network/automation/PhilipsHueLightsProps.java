package home.network.automation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "philips.lights")
@Getter
@Setter
public class PhilipsHueLightsProps {
    private List<String> livingroom;
    private List<String> hallway;
    private List<String> multimediaControl;
    private List<String> badWeatherNotify;
}
