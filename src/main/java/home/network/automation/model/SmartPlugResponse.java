package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SmartPlugResponse extends BroadlinkBridgeResponse {
    @JsonProperty("on_off_status")
    private String onOffStatus;
}
