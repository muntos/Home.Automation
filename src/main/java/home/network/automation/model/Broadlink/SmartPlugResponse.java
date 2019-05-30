package home.network.automation.model.Broadlink;

import com.fasterxml.jackson.annotation.JsonProperty;
import home.network.automation.model.PhilipsHue.BroadlinkBridgeResponse;
import lombok.Getter;

@Getter
public class SmartPlugResponse extends BroadlinkBridgeResponse {
    @JsonProperty("on_off_status")
    private String onOffStatus;
}
