package home.network.automation.model.PhilipsHue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class HueAmbientLightSensorState {
    @JsonProperty("lastupdated")
    @SerializedName("lastupdated")
    private String lastUpdated;

    @JsonProperty("lightlevel")
    @SerializedName("lightlevel")
    private Integer lightLevel;

    private Boolean dark;

    private Boolean daylight;
}

