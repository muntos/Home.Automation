package home.network.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class BroadlinkBridgeResponse {
    public enum status{
        ok,
        failed
    }
    private Long timestamp;

    private String deviceMac;

    private status status;

    private String msg;
}
