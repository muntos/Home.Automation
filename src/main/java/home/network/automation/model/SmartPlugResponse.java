package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SmartPlugResponse {
    public enum status{
        ok,
        failed
    }
    private Long timestamp;

    private String deviceMac;

    private String msg;

    @JsonProperty("on_off_status")
    private String onOffStatus;

    private status status;

}
