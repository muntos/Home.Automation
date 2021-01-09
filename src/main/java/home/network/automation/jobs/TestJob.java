package home.network.automation.jobs;

import home.network.automation.components.Kodi;
import home.network.automation.tasks.HueLightsControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class TestJob {

    @Autowired
    private HueLightsControl hueLightsControl;

    @Scheduled(fixedDelay = 50000)
    private void test() {
        hueLightsControl.eventReceived(Kodi.Event.PLAY_PAUSED);
    }
}
