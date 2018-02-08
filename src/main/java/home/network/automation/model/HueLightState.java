package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class HueLightState {
    private Boolean on;
    private int hue;
    private int ct;
    private String alert;
    private String effect;
    private Boolean reachable;

    @JsonProperty("sat")
    private short saturation;

    @JsonProperty("bri")
    private short brightness;



}
