package home.network.automation.devices.broadlink;

import com.github.mob41.blapi.A1Device;
import com.github.mob41.blapi.RM2Device;
import com.github.mob41.blapi.SP2Device;
import com.github.mob41.blapi.mac.Mac;
import home.network.automation.devices.broadlink.A1Sensor.A1Sensor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class BroadlinkDevicesTest {

    @Test
    public void testA1Sensor() throws Exception {
        A1Device dev = new A1Device("192.168.1.80", new Mac("34:ea:34:c7:61:f8"));

        boolean success = dev.auth();
        log.info("Auth status: " + (success ? "Success!" : "Failed!"));

        log.info("Temp: {}", dev.getSensorsData().getTemp());

        A1Sensor a1Sensor = new A1Sensor("A1 sensor", "A1", "34:ea:34:c7:61:f8", "192.168.1.80");

        a1Sensor.getReadings();

    }

    @Test
    public void testRMPro() throws Exception {
        RM2Device rm2Device = new RM2Device("192.168.1.30", new Mac("34:ea:34:bb:1b:9c"));

        boolean success = rm2Device.auth();
        log.info("Auth status: " + (success ? "Success!" : "Failed!"));

        log.info("RM2+ temp: {}", rm2Device.getTemp());
    }

    @Test
    public void testSP2Device() throws Exception {
        SP2Device sp2Device = new SP2Device("192.168.1.50", new Mac("34:ea:34:59:6a:f7"));

        boolean success = sp2Device.auth();
        log.info("Auth status: " + (success ? "Success!" : "Failed!"));

        Boolean state = sp2Device.getState();
        log.info("Plug state: {}", state);
        sp2Device.setState(!state);

    }

}
