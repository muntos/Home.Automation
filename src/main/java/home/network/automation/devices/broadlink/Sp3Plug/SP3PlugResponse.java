package home.network.automation.devices.broadlink.Sp3Plug;

import com.fasterxml.jackson.annotation.JsonProperty;
import home.network.automation.model.PhilipsHue.BroadlinkBridgeResponse;
import lombok.Getter;

@Getter
public class SP3PlugResponse extends BroadlinkBridgeResponse {
    @JsonProperty("on_off_status")
    private String onOffStatus;
}
