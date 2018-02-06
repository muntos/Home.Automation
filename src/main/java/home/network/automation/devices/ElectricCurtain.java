package home.network.automation.devices;

import lombok.Getter;

public class ElectricCurtain extends RemoteControlledDevice {
    @Getter
    private int waitOnEventBeforeOpen;
    @Getter
    private int waitOnEventBeforeClose;

    public ElectricCurtain(String name, String shortName, int waitOnEventBeforeOpen, int waitOnEventBeforeClose) {
        super(name, shortName, true);
        this.waitOnEventBeforeClose = waitOnEventBeforeClose;
        this.waitOnEventBeforeOpen = waitOnEventBeforeOpen;
    }
}
