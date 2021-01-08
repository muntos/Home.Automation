package home.network.automation.model.philipsHue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class HueMotionSensorState {
    @JsonProperty("lastupdated")
    @SerializedName("lastupdated")
    private String lastUpdated;
    private Boolean presence;
}

