package home.network.automation.devices.generic;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AudioDevice extends RemoteControlledDevice {
    private int startVolume;
    @Setter
    private int currentVolume;
    private Boolean isConnectedToLogitechMediaServer;

    public AudioDevice(String name, String shortName, int startVolume, Boolean isConnectedToLogitechMediaServer) {
        super(name, shortName);
        this.startVolume = startVolume;
        this.isConnectedToLogitechMediaServer = isConnectedToLogitechMediaServer;
    }

}
