package home.network.automation.devices;

import home.network.automation.model.BroadlinkBridgeResponse;
import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

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
        log.info("'{}' received Infrared ==> {} : {} (id={})", name, deviceName, button.getButtonName(), button.getCodeId());
        HashMap<String, String> values = new HashMap<>();
        values.put("codeId", String.valueOf(button.getCodeId()));
        BroadlinkBridgeResponse response = broadlinkBridge.setStatus(values, name, macAddress, BroadlinkBridgeResponse.class);
        if (response != null){
            Boolean success = response.getStatus().equals(BroadlinkBridgeResponse.status.ok);
            log.info("'{}' (MAC = {}) press button '{}' (codeId={}) returned {}", name, macAddress, button.getFriendlyName(), button.getCodeId(), success);
            return new CommandResult(success, response.getMsg());
        }

        return new CommandResult(false, String.format("Failed to press button '%s' on '%s' (MAC = %s)", button.getFriendlyName(), name, macAddress));
    }

    @Override
    public CommandResult pressButtonUsingRF(Button button) {
        log.info("'{}' received RF button id {} ({})", name, button.getCodeId(), button.getFriendlyName());
        return new CommandResult(true, "success");
    }

}
