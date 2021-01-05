package home.network.automation.devices.broadlink.Sp3Plug;

import com.github.mob41.blapi.SP2Device;
import com.github.mob41.blapi.mac.Mac;
import home.network.automation.devices.generic.SmartPlug;
import home.network.automation.model.CommandResult;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

@Slf4j
public class SP3Plug extends SmartPlug {

    private String macAddress;
    private String ipAddress;

    public SP3Plug(String name, String shortName, String macAddress, String ipAddress, int waitBeforeTurnOff, int minWaitBeforeStatesChange) {
        super(name, shortName, waitBeforeTurnOff, minWaitBeforeStatesChange);
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
    }

    public Status getStatus() {
        Status status = Status.UNKNOWN;
        try {
            SP2Device sp2Device = new SP2Device(ipAddress, new Mac(macAddress));
            if (!sp2Device.auth()) {
                log.error("Failed to authorize plug '{}' using MAC:{} and IP:{}", shortName, macAddress, ipAddress);
                return status;
            }

            status = SmartPlug.value(sp2Device.getState());
            log.info("'{}' (MAC = {}) status: {}", name, macAddress, status);
            return status;
        } catch (Exception ex) {
            log.error("Exception trying to get plug '{}' state using MAC:{} and IP:{}!", shortName, macAddress, ipAddress);
            return status;
        }
    }

    public CommandResult setStatusNow(Status status){
        try {
            SP2Device sp2Device = new SP2Device(ipAddress, new Mac(macAddress));
            if (!sp2Device.auth()) {
                String errorMsg = String.format("Failed to authorize plug '%s' using MAC:%s and IP:%s", shortName, macAddress, ipAddress);
                log.error(errorMsg);
                return new CommandResult(false, errorMsg);
            }

            sp2Device.setState(SmartPlug.value(status));
            lastStatusChange = new DateTime();
            return new CommandResult(true, String.format("Successfully set '%s' (MAC = %s) status to '%s'", name, macAddress, status));
        } catch (Exception ex) {
            log.error("Exception trying to set plug '{}' state using MAC:{} and IP:{}!", shortName, macAddress, ipAddress);
            return new CommandResult(false, String.format("Failed to set '%s' (MAC = %s) status to '%s'", name, macAddress, status));
        }
    }

}
