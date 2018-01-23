package home.network.automation.web;

import home.network.automation.model.CommandResult;
import home.network.automation.service.CommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commands")
public class DeviceController {
    @Autowired
    private CommandsService commandsService;

    @RequestMapping(value = "/press/{device}/{button}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public CommandResult pressRemoteButton(@PathVariable("device") String deviceName, @PathVariable("button") String buttonName) {
        return commandsService.pressRemoteButton(deviceName, buttonName);
    }

    @GetMapping("/lms/volume/{volumeValue}")
    public CommandResult changeLMSvolume(@PathVariable("volumeValue") String value){
        return commandsService.changedLogitechMediaServerVolume(value);
    }
}