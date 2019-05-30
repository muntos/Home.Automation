package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class HueLightState {
    private Boolean on;
    private Integer hue;
    private Integer ct;
    private String alert;
    private String effect;
    private Boolean reachable;
    private Float[] xy;

    @JsonProperty("sat")
    private Integer saturation;

    @JsonProperty("bri")
    private Integer brightness;

}
