package home.network.automation.tasks;

import external.logitech.harmony.ActivityChangeListener;
import external.logitech.harmony.ActivityStatusListener;
import external.logitech.harmony.HarmonyClient;
import external.logitech.harmony.config.Activity;
import home.network.automation.devices.logitech.HarmonyHub;
import home.network.automation.devices.tplink.TapoP100Plug;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static home.network.automation.devices.generic.SmartPlug.Status.OFF;
import static home.network.automation.devices.generic.SmartPlug.Status.ON;


@Slf4j
@Component
public class SmartPlugControl {
    private HarmonyClient harmonyClient;

    @Autowired
    private HarmonyHub harmonyHub;

    @Autowired
    @Qualifier("P100_H80")
    private TapoP100Plug p100_H80;

    @Autowired
    @Qualifier("P100_Rack_Vents_12V")
    private TapoP100Plug p100_12V;

    @PostConstruct
    public void init(){
        try {
            harmonyClient = harmonyHub.getHarmonyClient();
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
        controlPlugs(status);
    }

    public void controlPlugs(Activity.Status status){

        switch (status){
            case ACTIVITY_IS_STARTING:
                p100_H80.setStatusWithScheduler(ON);
                p100_12V.setStatusWithScheduler(ON);
                break;
            case HUB_IS_TURNING_OFF:
                p100_H80.setStatusWithScheduler(OFF);
                p100_12V.setStatusWithScheduler(OFF);
                break;
        }
    }

}
