package home.network.automation.devices.A1Sensor;

import home.network.automation.devices.generic.Device;
import home.network.automation.devices.api.BroadlinkBridge;

import java.util.Optional;

public class A1SensorLegacy extends Device {
    private String macAddress;
    private BroadlinkBridge broadlinkBridge;

    public A1SensorLegacy(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge){
        super(name, shortName);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
    }

    public Optional<A1ResponseLegacy> getReadings(){
        A1ResponseLegacy response = broadlinkBridge.getStatus(name, macAddress, "a1_status", A1ResponseLegacy.class);
        return Optional.ofNullable(response);
    }
}
