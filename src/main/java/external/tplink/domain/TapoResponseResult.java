package external.tplink.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class TapoResponseResult {
    @JsonProperty("device_on")
    private Boolean deviceOn;
    private Boolean overheated;
    private String type;
    private String model;
}
