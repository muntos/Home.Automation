package home.network.automation.devices.philips;

import home.network.automation.PhilipsHueLightsProps;
import home.network.automation.components.Kodi;
import home.network.automation.devices.api.HueApiClient;
import home.network.automation.model.philipsHue.HueLight;
import home.network.automation.tasks.HueLightsControl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest

public class PhilipsDevicesTest {

    @Autowired
    private PhilipsHueLightsProps philipsHueLightsProps;

    @Autowired
    private HueLightsControl hueLightsControl;

    @Test
    public void testPhilipsHueLightsProps() {
        assertTrue(philipsHueLightsProps.getLivingroom().size() > 0);
        assertTrue(philipsHueLightsProps.getHallway().size() > 0);
        assertTrue(philipsHueLightsProps.getMultimediaControl().size() > 0);
    }

    @Test
    public void testGetHueLights() {
        HueApiClient hueApiClient = new HueApiClient("http", "192.168.1.169", 80, "nJCp1QzRfxjkJ2GKVJYbBo0PRyQVZ5S5rTCq23Ot");
        HueBridge hueBridge = new HueBridge("Philips Hue Bridge", "Hue", hueApiClient);
        Map<String, HueLight> lights = hueBridge.getLights();
    }

    @Test
    public void testControlLivingRoomLights() throws InterruptedException{
        hueLightsControl.eventReceived(Kodi.Event.PLAY_PAUSED);
        Thread.sleep(4000);
        hueLightsControl.eventReceived(Kodi.Event.PLAY_RESUMED);
    }

}
