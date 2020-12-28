package home.network.automation.jobs;

import home.network.automation.devices.HarmonyHub;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import external.logitech.harmony.HarmonyClient;
import external.logitech.harmony.config.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("prod")
@Slf4j
@Component
public class CheckHarmonyHubStatusJob {
    private HarmonyClient harmonyClient;

    @Autowired
    private House house;

    @Scheduled(fixedDelay = 30000)
    public void doJob() {
        HarmonyHub harmonyHub = house.getDevice("Harmony");
        harmonyClient = harmonyHub.getHarmonyClient();
        try {
            Activity currentActivity = harmonyClient.getCurrentActivity();
        }catch (RuntimeException ex){
            log.error("Error when trying to check Harmony Hub status: {}", ex.getMessage());
            log.info("Trying to reconnect to Harmony Hub {}", harmonyHub.getAddress());
            try{
                harmonyClient.connect(harmonyHub.getAddress());
            } catch (RuntimeException e){
                log.warn("Failed connecting to Harmony Hub {} error is: {}", harmonyHub.getAddress(), e.getMessage());
            }
        }
    }

}
