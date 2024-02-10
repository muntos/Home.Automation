package home.network.automation.devices.philips;

import home.network.automation.devices.generic.SmartPlug;
import home.network.automation.model.CommandResult;
import home.network.automation.model.philipsHue.HueLight;
import home.network.automation.model.philipsHue.HueLightState;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

@Slf4j
public class HueSmartPlug extends SmartPlug {
    private HueBridge hueBridge;
    public HueSmartPlug(String name, String shortName, int waitBeforeTurnOff, int minWaitBeforeStatesChange, HueBridge hueBridge) {
        super(name, shortName, waitBeforeTurnOff, minWaitBeforeStatesChange);
        this.hueBridge = hueBridge;
    }

    @Override
    public CommandResult setStatusNow(Status status) {
        log.debug("Set status {} for plug '{}'", status, shortName);
        HueLightState state = new HueLightState();
        state.setOn(status == Status.ON ? true : false);
        return new CommandResult(hueBridge.setLight(shortName, state), Strings.EMPTY);
    }

    @Override
    public Status getStatus() {
        log.debug("Get Hue plug status for '{}'", shortName);
        HueLight plug = hueBridge.getLight(shortName);
        if (plug == null) {
            return Status.UNKNOWN;
        }
        log.debug("Hue plug status: {}", plug);
        return (plug.getState().getOn() ? Status.ON : Status.OFF);
    }
}
