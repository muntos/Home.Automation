package home.network.automation.tasks;

import home.network.automation.devices.HarmonyHub;
import home.network.automation.devices.broadlink.SmartPlug;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import net.whistlingfish.harmony.ActivityChangeListener;
import net.whistlingfish.harmony.ActivityStatusListener;
import net.whistlingfish.harmony.HarmonyClient;
import net.whistlingfish.harmony.config.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static home.network.automation.devices.broadlink.SmartPlug.Status.OFF;
import static home.network.automation.devices.broadlink.SmartPlug.Status.ON;

@Slf4j
@Component
public class SmartPlugControl {
    private HarmonyClient harmonyClient;

    @Autowired
    private House house;

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

        switch (status){
            case ACTIVITY_IS_STARTING:
                smartPlug.setStatusWithScheduler(ON);
                break;
            case HUB_IS_TURNING_OFF:
                smartPlug.setStatusWithScheduler(OFF);
                break;
        }
    }

}
