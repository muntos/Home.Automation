package home.network.automation.devices;

import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BroadlinkHub extends RemoteControlDevice {
    private String macAddress;

    private BroadlinkBridge broadlinkBridge;

    public BroadlinkHub(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge) {
        super(name, shortName);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
        canSendIR = true;
        canSendRF = true;
    }

    public BroadlinkHub(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge, Integer priority) {
        super(name, shortName);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
        canSendIR = true;
        canSendRF = true;
        setPriority(priority);
    }

    @Override
    public CommandResult pressButtonUsingInfrared(String deviceName, Button button) {
        log.info("'{}' received Infrared ==> {} : {} (id={})", name, deviceName, button.getButtonName(), button.getButtonId());
        return new CommandResult(true, "success");
    }

    @Override
    public CommandResult pressButtonUsingRF(Button button) {
        log.info("'{}' received RF button id {} ({})", name, button.getButtonId(), button.getFriendlyName());
        return new CommandResult(true, "success");
    }

}
