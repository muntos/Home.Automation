package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class HueBridgeError {
    private short type;
    private String address;
    private String description;
}
