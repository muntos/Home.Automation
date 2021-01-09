package home.network.automation.model.philipsHue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class HueLight {
    @Setter
    private HueLightState state;
    @Setter
    private Integer id;
    private String type;
    private String name;

    @JsonProperty("modelid")
    private String modelId;
}
