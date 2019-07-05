package home.network.automation.devices;

import lombok.Getter;

@Getter
public class NetworkAudioDevice extends AudioDevice {
    private String server;

    public NetworkAudioDevice(String name, String shortName, int startVolume, Boolean isConnectedToLogitechMediaServer, String server) {
        super(name, shortName, startVolume, isConnectedToLogitechMediaServer);
        this.server = server;
    }
}
