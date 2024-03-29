package home.network.automation.devices.logitech;

import com.google.inject.Guice;
import com.google.inject.Injector;
import external.logitech.harmony.HarmonyClient;
import external.logitech.harmony.HarmonyClientModule;
import home.network.automation.devices.generic.RemoteControlDevice;
import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HarmonyHub extends RemoteControlDevice {
    @Getter
    //@Inject
    private HarmonyClient harmonyClient;

    @Getter
    private String address;

    public HarmonyHub(String name, String shortName, String address, Boolean connect) {
        super(name, shortName);
        this.address = address;
        canSendIR = true;
        if (connect) {
            connect();
        }

    }

    public HarmonyHub(String name, String shortName, String address, Integer priority, Boolean connect) {
        super(name, shortName);
        this.address = address;
        canSendIR = true;
        setPriority(priority);
        this.harmonyClient = HarmonyClient.getInstance();
        if (connect) {
            connect();
        }
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
