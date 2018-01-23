package home.network.automation.service;

import home.network.automation.devices.RemoteControlDevice;
import home.network.automation.devices.RemoteControlledDevice;
import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommandsServiceImpl implements CommandsService {
    @Autowired
    House house;


    @Override
    public CommandResult pressRemoteButton(String deviceName, String buttonName) {
        RemoteControlledDevice device = house.getRemoteControlledDevice(deviceName);
        if (device == null) {
            log.error("No device with name '{}' could be found! Check your config.", deviceName);
            return new CommandResult(false, String.format("No device with name '%s' could be found! Check your config.", deviceName));
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

}
