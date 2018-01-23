package home.network.automation.observer;

import home.network.automation.devices.Device;

import java.util.ArrayList;
import java.util.List;

public class House {
    private List<Device> devices = new ArrayList<>();

    public House addDevice(Device device){
        devices.add(device);
        return this;
    }

    public List<Device> getDevices() {
        return devices;
    }
}
