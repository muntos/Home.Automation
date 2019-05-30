package home.network.automation.model.PhilipsHue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class HueBridgeErrorResponse {
    private HueBridgeError error;
}
