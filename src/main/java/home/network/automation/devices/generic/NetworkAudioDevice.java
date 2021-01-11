package home.network.automation.devices.generic;

import lombok.Getter;

@Getter
public class NetworkAudioDevice extends AudioDevice {
    private String address;

    public NetworkAudioDevice(String name, String shortName, int startVolume, Boolean isConnectedToLogitechMediaServer, String address) {
        super(name, shortName, startVolume, isConnectedToLogitechMediaServer);
        this.address = address;
    }
}
