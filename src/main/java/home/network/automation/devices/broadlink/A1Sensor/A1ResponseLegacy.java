package home.network.automation.devices.broadlink.A1Sensor;

import home.network.automation.devices.broadlink.RMPlugin.BroadlinkBridgeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class A1ResponseLegacy extends BroadlinkBridgeResponse {
    private Double temperature;
    private Double light;
    private Double noisy;
    private Double humidity;
    private Double air;
}
