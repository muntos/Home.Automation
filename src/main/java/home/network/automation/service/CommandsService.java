package home.network.automation.service;

import home.network.automation.devices.generic.NetworkAudioDevice;
import home.network.automation.model.CommandResult;

public interface CommandsService {
    CommandResult pressRemoteButton(String deviceName, String buttonName);
    CommandResult changedLogitechMediaServerVolume(String volumeValue);
    void turnOnLogitechMediaServer();
    void turnOffLogitechMediaServer();
    void telnetConnect(NetworkAudioDevice networkAudioDevice);
}
