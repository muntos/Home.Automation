package home.network.automation.model.PhilipsHue;

import lombok.Getter;

@Getter
public class A1SensorResponse extends BroadlinkBridgeResponse {
    private Double temperature;
    private Double light;
    private Double noisy;
    private Double humidity;
    private Double air;
}
