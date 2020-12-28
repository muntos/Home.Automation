package home.network.automation.devices;


import home.network.automation.devices.generic.Device;
import home.network.automation.model.Button;
import home.network.automation.model.CommandResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class RemoteControlDevice extends Device {
    //highest value has highest priority
    @Getter()
    @Setter(AccessLevel.PROTECTED)
    private Integer priority = 1;

    @Getter
    protected Boolean canSendIR = false;
    @Getter
    protected Boolean canSendRF = false;


    public RemoteControlDevice(String name, String shortName) {
        super(name, shortName);
    }

    public abstract CommandResult pressButtonUsingInfrared(String deviceName, Button button);
    public abstract CommandResult pressButtonUsingRF(Button button);

}
