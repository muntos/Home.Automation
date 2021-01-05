package home.network.automation.devices.broadlink.Sp3Plug;

import home.network.automation.devices.api.BroadlinkBridge;
import home.network.automation.devices.generic.SmartPlug;
import home.network.automation.model.CommandResult;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.HashMap;

@Slf4j
public class SP3PlugLegacy extends SmartPlug {

    private String macAddress;
    private BroadlinkBridge broadlinkBridge;

    public SP3PlugLegacy(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge, int waitBeforeTurnOff, int minWaitBeforeStatesChange) {
        super(name, shortName, waitBeforeTurnOff, minWaitBeforeStatesChange);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
    }

    public Status getStatus(){
        Status status = Status.UNKNOWN;
        SP3PlugResponseLegacy response = broadlinkBridge.getStatus(name, macAddress, "status", SP3PlugResponseLegacy.class);
        if (response != null){
            if (response.getStatus().equals(SP3PlugResponseLegacy.status.ok)){
                status = value(response.getOnOffStatus());
            }
        }
        log.info("'{}' (MAC = {}) status: {}", name, macAddress, status);
        return status;
    }

    public CommandResult setStatusNow(Status status){
        HashMap<String, String> values = new HashMap<>();
        values.put("on", value(status).toString());
        SP3PlugResponseLegacy response = broadlinkBridge.sendCommand(values, name, macAddress, SP3PlugResponseLegacy.class);
        if (response != null){
            Boolean success = response.getStatus().equals(SP3PlugResponseLegacy.status.ok);
            log.info("'{}' (MAC = {}) set status to '{}' returned {}", name, macAddress, status, success);
            if (success){
                lastStatusChange = new DateTime();
            }
            return new CommandResult(success, response.getMsg());
        }

        return new CommandResult(false, String.format("Failed to set '%s' (MAC = %s) status to '%s'", name, macAddress, status));
    }

}
