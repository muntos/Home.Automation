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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

    @Value("${hegel.h80.powerOnOff.wait.seconds}")
    private int h80PowerOnOffWaitTIme;

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
                    processActivityEvents(activity, status);
                }
            });
        } catch (RuntimeException ex){

        }
    }

    private void processActivityEvents(Activity activity, Activity.Status status){
        controlH80Plug(activity, status);
    }

    private void controlH80Plug(Activity activity, Activity.Status status){
        String plugName = "SP3_H80";
        SmartPlug smartPlug = house.getDevice(plugName);
        if (smartPlug == null){
            log.error("Could not find any smart plug named '{}', check your configuration!", plugName);
            return;
        }
        if (status == ACTIVITY_IS_STARTING){
            int sec = smartPlug.secondsSinceLastStatusChange();
            if (sec > h80PowerOnOffWaitTIme) {
                log.info("Power on '{}' plug!", plugName);
                smartPlug.setStatus(ON);
            } else{
                scheduleSmartPlugAction(smartPlug, ON, h80PowerOnOffWaitTIme - sec);
            }
        } else if (status == HUB_IS_TURNING_OFF){
            scheduleSmartPlugAction(smartPlug, OFF, 20);
        }


    }

    private void scheduleSmartPlugAction(SmartPlug smartPlug, SmartPlug.Status status, int delay){
        log.info("Schedule '{}' to {} in {} seconds", smartPlug.getName(), status, delay);
        ScheduledFuture<?> future = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                smartPlug.setStatus(status);
            }}, delay, TimeUnit.SECONDS);
        futures.put(smartPlug.getName(), future);
    }
}
