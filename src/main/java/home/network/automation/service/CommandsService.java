package home.network.automation.service;

import home.network.automation.model.CommandResult;

public interface CommandsService {
    CommandResult pressRemoteButton(String deviceName, String commandName);
}
