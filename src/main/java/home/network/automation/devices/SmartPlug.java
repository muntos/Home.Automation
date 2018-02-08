package home.network.automation.devices;

import home.network.automation.model.CommandResult;
import home.network.automation.model.SmartPlugResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static home.network.automation.devices.SmartPlug.Status.OFF;
import static home.network.automation.devices.SmartPlug.Status.ON;

@Slf4j
public class SmartPlug extends Device {
    private Map<String, ScheduledFuture> futures = new HashMap<>();

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public enum Status {
        ON,
        OFF,
        UNKNOWN;
    }

    private static Status value(String status){
        switch (status){
            case "0":
                return OFF;
            case "1":
                return ON;
            default:
                return Status.UNKNOWN;
        }
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
    //number of seconds to wait (after command is received) before turn off
    @Getter
    private int waitBeforeTurnOff;
    //minimum number of seconds to wait before states change (from On->Off or from Off->On
    @Getter
    private int minWaitBeforeStatesChange;

    public SmartPlug(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge, int waitBeforeTurnOff, int minWaitBeforeStatesChange) {
        super(name, shortName);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
        this.waitBeforeTurnOff = waitBeforeTurnOff;
        this.minWaitBeforeStatesChange = minWaitBeforeStatesChange;
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

    public void setStatusWithScheduler(Status status){
        Future existingFuture = futures.get(name);
        if (existingFuture != null){
            log.info("Found an existing schedule for '{}' plug, cancelling now!", name);
            existingFuture.cancel(false);
            futures.remove(name);
        }

        if (status == ON){
            Status currentStatus = getStatus();
            if (currentStatus == ON){
                log.info("Plug '{}' already ON, nothing to do.", name);
                return;
            }
            int sec = secondsSinceLastStatusChange();
            if (sec > minWaitBeforeStatesChange) {
                log.info("Power on '{}' plug!", name);
                setStatusNow(ON);
            } else{
                scheduleSmartPlugAction(ON, minWaitBeforeStatesChange - sec);
            }
        } else if (status == OFF){
            scheduleSmartPlugAction(OFF, waitBeforeTurnOff);
        }

    }

    private CommandResult setStatusNow(Status status){
        HashMap<String, String> values = new HashMap<>();
        values.put("on", value(status).toString());
        SmartPlugResponse response = broadlinkBridge.sendCommand(values, name, macAddress, SmartPlugResponse.class);
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

    private void scheduleSmartPlugAction(Status status, int delay){
        log.info("Schedule '{}' to {} in {} seconds", name, status, delay);
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            setStatusNow(status);
            futures.remove(name);
        }, delay, TimeUnit.SECONDS);
        futures.put(name, future);
    }

}
