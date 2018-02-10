package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class HueLightState {
    private Boolean on;
    private Integer hue;
    private Integer ct;
    private String alert;
    private String effect;
    private Boolean reachable;

    @JsonProperty("sat")
    private Integer saturation;

    @JsonProperty("bri")
    private Integer brightness;



}
