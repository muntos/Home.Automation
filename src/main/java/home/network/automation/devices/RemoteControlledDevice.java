package home.network.automation.devices;


import home.network.automation.devices.generic.Device;
import home.network.automation.model.Button;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class RemoteControlledDevice extends Device {
    private Boolean receiveRF = false;
    private List<Button> buttons = new ArrayList<>();
    private RemoteControlDevice preferredRemote;

    public RemoteControlledDevice(String name, String shortName) {
        super(name, shortName);
    }

    public RemoteControlledDevice(String name, String shortName, Boolean receiveRF) {
        super(name, shortName);
        this.receiveRF = receiveRF;
    }

    public RemoteControlledDevice addButton(Button button){
        buttons.add(button);
        return this;
    }

    public RemoteControlledDevice setPrefferredRemote(RemoteControlDevice remote){
        if (remote != null) {
            log.info("Setting remote '{}' as preferred for '{}'", remote.getName(), name);
            this.preferredRemote = remote;
        } else {
            log.warn("Could not set a preferred remote for '{}', will use default remote!", name);
        }
        return this;
    }

    public Button getButton(String buttonName){
        return buttons.stream()
                .filter(x -> x.getButtonName().equalsIgnoreCase(buttonName) || x.getFriendlyName().equalsIgnoreCase(buttonName))
                .findFirst()
                .orElse(null);
    }

    public Button getButton(Button.Mapping mapping){
        return buttons.stream()
                .filter(x -> x.getMapping().equals(mapping))
                .findFirst()
                .orElse(null);
    }

}
