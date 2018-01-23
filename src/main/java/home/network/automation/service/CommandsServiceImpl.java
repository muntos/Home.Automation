package home.network.automation.service;

import home.network.automation.devices.SendInfrared;
import home.network.automation.model.CommandResult;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommandsServiceImpl implements CommandsService {
    @Autowired
    House house;

    SendInfrared sendInfrared;

    @Override
    public CommandResult pressRemoteButton(String deviceName, String commandName) {
       return null;
    }

}
