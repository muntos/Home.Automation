package home.network.automation.devices;

import home.network.automation.devices.RemoteControlDevice;
import home.network.automation.devices.api.BroadlinkBridge;
import home.network.automation.model.PhilipsHue.BroadlinkBridgeResponse;
import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class BroadlinkHub extends RemoteControlDevice {
    private String macAddress;
    private final int MAX_RETRIES = 3;

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
        return pressButton(button);
    }

    @Override
    public CommandResult pressButtonUsingRF(Button button) {
        log.info("'{}' received RF button id {} ({})", name, button.getCodeId(), button.getFriendlyName());
        return pressButton(button);
    }

    private CommandResult pressButton(Button button){
        HashMap<String, String> values = new HashMap<>();
        values.put("codeId", String.valueOf(button.getCodeId()));

        Boolean success = false;
        int retries = 0;
        while (!success  && retries < MAX_RETRIES ) {
            retries++;
            BroadlinkBridgeResponse response = broadlinkBridge.sendCommand(values, name, macAddress, BroadlinkBridgeResponse.class);
            if (response != null) {
                success = response.getStatus().equals(BroadlinkBridgeResponse.status.ok);
                if (success) {
                    log.info("'{}' (MAC = {}) press button '{}' (codeId={}) returned {}", name, macAddress, button.getFriendlyName(), button.getCodeId(), success);
                    return new CommandResult(success, response.getMsg());
                } else {
                    log.warn("'{}' (MAC = {}) press button '{}' (codeId={}) returned {}", name, macAddress, button.getFriendlyName(), button.getCodeId(), success);
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

        log.error("Failed to press button '{}' on '{}' (MAC = {}), giving up after {} retries", button.getFriendlyName(), name, macAddress, MAX_RETRIES);
        return new CommandResult(false, String.format("Failed to press button '%s' on '%s' (MAC = %s)", button.getFriendlyName(), name, macAddress));
    }
}
