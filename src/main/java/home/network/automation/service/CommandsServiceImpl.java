package home.network.automation.service;

import home.network.automation.devices.AudioDevice;
import home.network.automation.devices.NetworkAudioDevice;
import home.network.automation.devices.RemoteControlDevice;
import home.network.automation.devices.RemoteControlledDevice;
import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import home.network.automation.observer.House;
import home.network.automation.tasks.SmartPlugControl;
import lombok.extern.slf4j.Slf4j;
import net.whistlingfish.harmony.config.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommandsServiceImpl implements CommandsService {
    @Autowired
    private House house;
    @Autowired
    private SmartPlugControl smartPlugControl;

    @Override
    public CommandResult pressRemoteButton(String deviceName, String buttonName) {
        RemoteControlledDevice device = house.getDevice(deviceName);
        if (device == null) {
            log.error("No remote control device with name '{}' could be found! Check your config.", deviceName);
            return new CommandResult(false, String.format("No remote control device with name '%s' could be found! Check your config.", deviceName));
        }

       RemoteControlDevice remote =  house.getPreferredRemoteControlDevice(device.getReceiveRF());
       if (remote == null){
           log.error("Couldn't find and remote in your house!");
           return new CommandResult(false, "Couldn't find and remote in your house!");
       }

       Button button = device.getButton(buttonName);
       if (button == null) {
           log.error("No button with name '{}' could be found for device '{}'! Check your config", buttonName, deviceName);
           return new CommandResult(false, String.format("No button with name '%s' could be found for device '%s'! Check your config", buttonName, deviceName));
       }

       remote = (device.getPreferredRemote() == null) ? remote : device.getPreferredRemote();
       log.info("Using remote '{}' for controlling device '{}'", remote.getName(), deviceName);
       if (device.getReceiveRF()){
           return remote.pressButtonUsingRF(button);
       } else {
           return remote.pressButtonUsingInfrared(deviceName, button);
       }

    }

    @Override
    public CommandResult changedLogitechMediaServerVolume(String value) {
        log.info("Received volume change request from Logitech Media Server with value {}", value);
        int volume = Integer.valueOf(value);
        CommandResult commandResult = new CommandResult(false, "unknown");
        AudioDevice audioAmplifier = house.getAudioDeviceConnectedToLogitechMediaServer();
        if (audioAmplifier == null) {
            log.error("Could not find any audio device connected to Logitech Media Server!");
            return new CommandResult(false, "Could not find any audio device connected to Logitech Media Server!");
        }

        Button volumeUp = audioAmplifier.getButton(Button.Mapping.VOLUME_UP);
        Button volumeDown = audioAmplifier.getButton(Button.Mapping.VOLUME_DOWN);
        if (volumeUp == null || volumeDown == null){
            log.error("Could not find mapping for volumeUp or volumeDown for device '{}', check configuration!", audioAmplifier.getName());
            return new CommandResult(false, String.format("Could not find mapping for volumeUp or volumeDown for device '%s', check configuration!", audioAmplifier.getName()));
        }

        if (volume > audioAmplifier.getCurrentVolume() || volume == 100) {
            commandResult = pressRemoteButton(audioAmplifier.getName(), volumeUp.getButtonName());
        }
        else if (volume < audioAmplifier.getCurrentVolume() || volume == 0){
            commandResult = pressRemoteButton(audioAmplifier.getName(), volumeDown.getButtonName());
        }

        if (commandResult.getSuccess()) {
            audioAmplifier.setCurrentVolume(volume);
        }

        return commandResult;
    }

    @Override
    public void turnOnLogitechMediaServer() {
        log.info("Received Turn on command from LMS");
        smartPlugControl.controlH80Plug(Activity.Status.ACTIVITY_IS_STARTING);
    }

    @Override
    public void turnOffLogitechMediaServer() {
        log.info("Received Turn off command from LMS");
        smartPlugControl.controlH80Plug(Activity.Status.HUB_IS_TURNING_OFF);
    }

    @Override
    public void telnetConnect(NetworkAudioDevice networkAudioDevice) {

    }
}
