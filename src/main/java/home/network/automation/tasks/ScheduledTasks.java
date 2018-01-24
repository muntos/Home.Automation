package home.network.automation.tasks;

import home.network.automation.devices.SmartPlug;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ScheduledTasks {
    @Autowired
    private House house;

    @Scheduled(fixedDelay = 10000)
    public void checkHarmonyActivity() {
        SmartPlug smartPlug = house.getDevice("SP3_H80");

        if (smartPlug.getStatus() == SmartPlug.Status.ON) {
            smartPlug.setStatus(false);
        } else {
            smartPlug.setStatus(true);
        }
        log.info("Device '{}' has status {}", smartPlug.getName(), smartPlug.getStatus());
    }

}