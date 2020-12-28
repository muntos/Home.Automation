package home.network.automation.tasks;

import external.logitech.harmony.ActivityChangeListener;
import external.logitech.harmony.ActivityStatusListener;
import external.logitech.harmony.HarmonyClient;
import external.logitech.harmony.config.Activity;
import home.network.automation.devices.HarmonyHub;
import home.network.automation.devices.generic.SmartPlug;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static home.network.automation.devices.generic.SmartPlug.Status.OFF;
import static home.network.automation.devices.generic.SmartPlug.Status.ON;


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
        SmartPlug tapoP100Plug = house.getDevice("P100_H80");

        if (tapoP100Plug == null){
            log.error("Could not find any smart plug named '{}', check your configuration!", "P100_H80");
            return;
        }

        switch (status){
            case ACTIVITY_IS_STARTING:
                tapoP100Plug.setStatusWithScheduler(ON);
                break;
            case HUB_IS_TURNING_OFF:
                tapoP100Plug.setStatusWithScheduler(OFF);
                break;
        }
    }

}
