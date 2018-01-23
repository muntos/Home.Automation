package home.network.automation.devices;

import home.network.automation.model.CommandResult;

public interface SendInfrared {
    CommandResult sendInfraredCommand(String deviceName, String commandName);
}
