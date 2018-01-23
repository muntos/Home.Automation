package home.network.automation.observer;

import home.network.automation.devices.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class House {
    private List<Device> devices = new ArrayList<>();

    public House addDevice(Device device){
        devices.add(device);
        return this;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<RemoteControlDevice> getRemoteControlDevices(){
        return devices.stream()
                .filter(x -> x instanceof RemoteControlDevice)
                .map( x -> (RemoteControlDevice) x)
                .collect(Collectors.toList());
    }

    public List<RemoteControlledDevice> getRemoteControlledDevices(){
        return devices.stream()
                .filter(x -> x instanceof RemoteControlledDevice)
                .map(x -> (RemoteControlledDevice) x)
                .collect(Collectors.toList());
    }

    public RemoteControlDevice getPreferredRemoteControlDevice(Boolean receiveRF){
        return getRemoteControlDevices().stream()
                .filter(x -> x.getCanSendRF() == receiveRF)
                .max(Comparator.comparing(RemoteControlDevice::getPriority))
                .orElse(null);
    }

    public RemoteControlDevice getRemoteControlDevice(String name){
        return getRemoteControlDevices().stream()
                .filter(x -> x.getShortName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

 /*   public <T extends Device> T getDevice(String deviceName, Class<T> type){
        return getDevices().stream()
                .filter(x -> x instanceof type)
                .map(x -> (type) x)
                .filter(x -> x.getShortName().equalsIgnoreCase(deviceName) || x.getName().equalsIgnoreCase(deviceName))
                .findFirst()
                .orElse(null);
    }*/

    public SmartPlug getSmartPlugDevice(String deviceName){
        return  devices.stream()
                .filter(x -> x instanceof SmartPlug)
                .map(x -> (SmartPlug) x)
                .filter(x -> x.getName().equalsIgnoreCase(deviceName) || x.getShortName().equalsIgnoreCase(deviceName))
                .findFirst()
                .orElse(null);
    }

    public RemoteControlledDevice getRemoteControlledDevice(String deviceName){
        return getRemoteControlledDevices()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(deviceName) || x.getShortName().equalsIgnoreCase(deviceName))
                .findFirst()
                .orElse(null);
    }

    public AudioDevice getAudioDeviceConnectedToLogitechMediaServer(){
        return devices.stream()
                .filter(x -> x instanceof  AudioDevice)
                .map(x -> (AudioDevice) x)
                .filter(x -> x.getIsConnectedToLogitechMediaServer())
                .findFirst()
                .orElse(null);
    }

}
