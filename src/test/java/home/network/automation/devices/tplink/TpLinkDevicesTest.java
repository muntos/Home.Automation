package home.network.automation.devices.tplink;

import home.network.automation.devices.generic.SmartPlug;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import static home.network.automation.devices.generic.SmartPlug.Status.OFF;
import static home.network.automation.devices.generic.SmartPlug.Status.ON;

@Slf4j
@Ignore
public class TpLinkDevicesTest {

    @Test
    public void testTapoP100Plug() {
        TapoP100Plug tapoP100Plug = new TapoP100Plug("Tapo P100", "P100", "192.168.1.73", new TapoLogin("alex.munteanu@gmail.com", "v8aD364VM6XWY_W"),0, 0);
        SmartPlug.Status status = tapoP100Plug.getStatus();
        log.info("Plug state is {}", status);
        tapoP100Plug.setStatusNow((status == ON) ? OFF : ON);
    }
}
