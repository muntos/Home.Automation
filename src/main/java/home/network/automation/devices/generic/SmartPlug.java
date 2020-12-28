package home.network.automation.devices.generic;


import home.network.automation.model.CommandResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static home.network.automation.devices.generic.SmartPlug.Status.OFF;
import static home.network.automation.devices.generic.SmartPlug.Status.ON;
import static home.network.automation.devices.generic.SmartPlug.Status.UNKNOWN;

@Slf4j
public abstract class SmartPlug extends Device {

    private Map<String, ScheduledFuture> futures = new HashMap<>();

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public enum Status {
        ON,
        OFF,
        UNKNOWN;
    }

    public static Status value(String status){
        switch (status){
            case "0":
                return OFF;
            case "1":
                return ON;
            default:
                return Status.UNKNOWN;
        }
    }

    public Boolean value(Status status){
        switch (status){
            case ON:
                return true;
            case OFF:
                return false;
            default:
                return true;
        }
    }

    public static Status value(Boolean status) {
        return (status == null) ? UNKNOWN : (status == true) ? ON : OFF;
    }

    protected DateTime lastStatusChange = new DateTime();
    //number of seconds to wait (after command is received) before turn off
    @Getter
    private int waitBeforeTurnOff;
    //minimum number of seconds to wait before states change (from On->Off or from Off->On
    @Getter
    private int minWaitBeforeStatesChange;

    public SmartPlug(String name, String shortName, int waitBeforeTurnOff, int minWaitBeforeStatesChange) {
        super(name, shortName);
        this.waitBeforeTurnOff = waitBeforeTurnOff;
        this.minWaitBeforeStatesChange = minWaitBeforeStatesChange;
    }

    public abstract CommandResult setStatusNow(Status status);

    public abstract Status getStatus();


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
