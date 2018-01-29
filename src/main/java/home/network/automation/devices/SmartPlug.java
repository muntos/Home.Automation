package home.network.automation.devices;

import home.network.automation.model.CommandResult;
import home.network.automation.model.SmartPlugResponse;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

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

    private Boolean value(Status status){
        switch (status){
            case ON:
                return true;
            case OFF:
                return false;
            default:
                return true;
        }
    }

    private String macAddress;
    private BroadlinkBridge broadlinkBridge;
    private DateTime lastStatusChange = new DateTime();

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
        return status;
    }

    public CommandResult setStatus(Status status){
        HashMap<String, String> values = new HashMap<>();
        values.put("on", value(status).toString());
        SmartPlugResponse response = broadlinkBridge.setStatus(values, name, macAddress, SmartPlugResponse.class);
        if (response != null){
            Boolean success = response.getStatus().equals(SmartPlugResponse.status.ok);
            log.info("'{}' (MAC = {}) set status to '{}' returned {}", name, macAddress, status, success);
            if (success){
                lastStatusChange = new DateTime();
            }
            return new CommandResult(success, response.getMsg());
        }

        return new CommandResult(false, String.format("Failed to set '%s' (MAC = %s) status to '%s'", name, macAddress, status));
    }

    public int secondsSinceLastStatusChange(){
        Seconds seconds = Seconds.secondsBetween(lastStatusChange, new DateTime());
        return seconds.getSeconds();
    }

}
