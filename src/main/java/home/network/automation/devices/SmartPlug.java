package home.network.automation.devices;

import home.network.automation.model.CommandResult;
import home.network.automation.model.SmartPlugResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class SmartPlug extends Device {
    public enum Status {
        ON,
        OFF,
        UNKNOWN;
    }

    private static Status value(String status){
        switch (status){
            case "0":
                return Status.OFF;
            case "1":
                return Status.ON;
            default:
                return Status.UNKNOWN;
        }
    }

    private static Status value(Boolean on){
        return (on) ? Status.ON : Status.OFF;
    }

    private Status status;
    private String macAddress;
    private BroadlinkBridge broadlinkBridge;

    public SmartPlug(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge) {
        super(name, shortName);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
    }

    public Status getStatus(){
        Status status = Status.UNKNOWN;
        SmartPlugResponse response = broadlinkBridge.getStatus(name, macAddress, SmartPlugResponse.class);
        if (response != null){
            if (response.getStatus().equals(SmartPlugResponse.status.ok)){
                status = value(response.getOnOffStatus());
            }
        }
        log.info("'{}' (MAC = {}) status: {}", name, macAddress, status);
        this.status = status;
        return status;
    }

    public CommandResult setStatus(Boolean on){
        HashMap<String, String> values = new HashMap<>();
        values.put("on", on.toString());
        SmartPlugResponse response = broadlinkBridge.setStatus(values, name, macAddress, SmartPlugResponse.class);
        if (response != null){
            Boolean success = response.getStatus().equals(SmartPlugResponse.status.ok);
            log.info("'{}' (MAC = {}) set status to '{}' returned {}", name, macAddress, on, success);
            this.status = value(on);
            return new CommandResult(success, response.getMsg());
        }

        return new CommandResult(false, String.format("Failed to set '%s' (MAC = %s) status to '%s'", name, macAddress, on));
    }

}
