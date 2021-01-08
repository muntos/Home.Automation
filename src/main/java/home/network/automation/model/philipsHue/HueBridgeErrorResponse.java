package home.network.automation.model.philipsHue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class HueBridgeErrorResponse {
    private HueBridgeError error;
}
