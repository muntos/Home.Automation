package home.network.automation.devices;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.whistlingfish.harmony.HarmonyClient;
import net.whistlingfish.harmony.HarmonyClientModule;

@Slf4j
public class HarmonyHub extends RemoteControlDevice{
    @Getter
    @Inject
    private HarmonyClient harmonyClient;

    @Getter
    private String address;

    public HarmonyHub(String name, String shortName, String address) {
        super(name, shortName);
        this.address = address;
        canSendIR = true;
        connect();
    }

    public HarmonyHub(String name, String shortName, String address, Integer priority) {
        super(name, shortName);
        this.address = address;
        canSendIR = true;
        setPriority(priority);
        connect();
    }

    private void connect(){
        try {
            Injector injector = Guice.createInjector(new HarmonyClientModule());
            injector.injectMembers(this);
            harmonyClient.connect(address);
            log.info("Connected to Harmony Hub {}", address);
        } catch (RuntimeException ex){
            log.warn("Failed connecting to Harmony Hub {} error is: {}", address, ex.getMessage());
        }
    }

    @Override
    public CommandResult pressButtonUsingInfrared(String deviceName, Button button) {
        log.info("'{}' received Infrared ==> {} : {}", name, deviceName, button.getButtonName());
        try {
            harmonyClient.pressButton(deviceName, button.getButtonName());
        } catch (Exception ex){
            log.error("Error: {}", ex.getMessage());
            return new CommandResult(false, ex.getMessage());
        }
        return new CommandResult(true, "success");
    }

    @Override
    public CommandResult pressButtonUsingRF(Button button) {
        log.info("'{}' received RF code id {} ({})", name, button.getCodeId(), button.getFriendlyName());
        log.error("This remote can't control RF devices!");
        return new CommandResult(false, "This remote can't control RF devices!");
    }

}
