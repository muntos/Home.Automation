package home.network.automation.devices;

import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HarmonyHub extends RemoteControlDevice{
    private String address;

    public HarmonyHub(String name, String shortName, String address) {
        super(name, shortName);
        this.address = address;
        canSendIR = true;
    }

    public HarmonyHub(String name, String shortName, String address, Integer priority) {
        super(name, shortName);
        this.address = address;
        canSendIR = true;
        setPriority(priority);
    }

    @Override
    public CommandResult pressButtonUsingInfrared(String deviceName, Button button) {
        log.info("'{}' received Infrared ==> {} : {}", name, deviceName, button.getButtonName());
        return new CommandResult(true, "success");
    }

    @Override
    public CommandResult pressButtonUsingRF(Button button) {
        log.info("'{}' received RF button id {} ({})", name, button.getButtonId(), button.getFriendlyName());
        log.error("This remote can't control RF devices!");
        return new CommandResult(false, "This remote can't control RF devices!");
    }

}
