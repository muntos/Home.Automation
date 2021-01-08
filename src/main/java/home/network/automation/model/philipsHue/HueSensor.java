package home.network.automation.model.philipsHue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public abstract class HueSensor {
    @Setter
    private Integer id;
    private String name;
    private String type;
    @JsonProperty("manufacturername")
    @SerializedName("manufacturername")
    private String manufacturerName;
}
