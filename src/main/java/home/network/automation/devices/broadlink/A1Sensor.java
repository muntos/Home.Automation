package home.network.automation.devices.broadlink;

import home.network.automation.devices.Device;
import home.network.automation.devices.api.BroadlinkBridge;
import home.network.automation.model.PhilipsHue.A1SensorResponse;

import java.util.Optional;

public class A1Sensor extends Device {
    private String macAddress;
    private BroadlinkBridge broadlinkBridge;

    public A1Sensor(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge){
        super(name, shortName);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
    }

    public Optional<A1SensorResponse> getReadings(){
        A1SensorResponse response = broadlinkBridge.getStatus(name, macAddress, "a1_status", A1SensorResponse.class);
        return Optional.ofNullable(response);
    }
}
