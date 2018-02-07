package home.network.automation.tasks;

import home.network.automation.devices.HarmonyHub;
import home.network.automation.devices.SmartPlug;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import net.whistlingfish.harmony.ActivityChangeListener;
import net.whistlingfish.harmony.ActivityStatusListener;
import net.whistlingfish.harmony.HarmonyClient;
import net.whistlingfish.harmony.config.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static home.network.automation.devices.SmartPlug.Status.OFF;
import static home.network.automation.devices.SmartPlug.Status.ON;
import static net.whistlingfish.harmony.config.Activity.Status.ACTIVITY_IS_STARTING;
import static net.whistlingfish.harmony.config.Activity.Status.HUB_IS_TURNING_OFF;

@Slf4j
@Component
public class SmartPlugControl {
    private HarmonyClient harmonyClient;

    @Autowired
    private House house;

    private Map<String, ScheduledFuture> futures = new HashMap<>();

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init(){
        try {
            harmonyClient = ((HarmonyHub)house.getDevice("Harmony")).getHarmonyClient();
            harmonyClient.addListener(new ActivityChangeListener() {
                @Override
                public void activityStarted(Activity activity) {
                    //log.info("activity changed: [{}] {}", activity.getId(), activity.getLabel());
                }
            });

            harmonyClient.addListener(new ActivityStatusListener() {
                @Override
                public void activityStatusChanged(Activity activity, Activity.Status status) {
                    log.info("activity status changed: [{}] {} - {}", activity.getId(), activity.getLabel(), status.toString());
                    processActivityEvents(status);
                }
            });
        } catch (RuntimeException ex){
                log.error("Exception in SmartPlugControl init(): {}", ex.getMessage());
        }
    }

    private void processActivityEvents(Activity.Status status){
        controlH80Plug(status);
    }

    public void controlH80Plug(Activity.Status status){
        String plugName = "SP3_H80";
        SmartPlug smartPlug = house.getDevice(plugName);
        if (smartPlug == null){
            log.error("Could not find any smart plug named '{}', check your configuration!", plugName);
            return;
        }

        if (status == ACTIVITY_IS_STARTING || status == HUB_IS_TURNING_OFF){
            Future existingFuture = futures.get(smartPlug.getName());
            if (existingFuture != null){
                log.info("Found an existing schedule for '{}' plug, cancelling now!", smartPlug.getName());
                existingFuture.cancel(false);
                futures.remove(smartPlug.getName());
            }
        }

        if (status == ACTIVITY_IS_STARTING){
            SmartPlug.Status currentStatus = smartPlug.getStatus();
            if (currentStatus == SmartPlug.Status.ON){
                log.info("Plug '{}' already ON, nothing to do.", smartPlug.getName());
                return;
            }
            int sec = smartPlug.secondsSinceLastStatusChange();
            if (sec > smartPlug.getMinWaitBeforeStatesChange()) {
                log.info("Power on '{}' plug!", plugName);
                smartPlug.setStatus(ON);
            } else{
                scheduleSmartPlugAction(smartPlug, ON, smartPlug.getMinWaitBeforeStatesChange() - sec);
            }
        } else if (status == HUB_IS_TURNING_OFF){
            scheduleSmartPlugAction(smartPlug, OFF, smartPlug.getWaitBeforeTurnOff());
        }
    }

    private void scheduleSmartPlugAction(SmartPlug smartPlug, SmartPlug.Status status, int delay){
        log.info("Schedule '{}' to {} in {} seconds", smartPlug.getName(), status, delay);
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            smartPlug.setStatus(status);
            futures.remove(smartPlug.getName());
        }, delay, TimeUnit.SECONDS);
        futures.put(smartPlug.getName(), future);
    }
}
