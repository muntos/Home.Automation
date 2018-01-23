package home.network.automation.devices;

import home.network.automation.model.CommandResult;

public class BroadlinkHub extends Device implements SendInfrared {
    private String macAddress;
    private BroadlinkBridge broadlinkBridge;

    public BroadlinkHub(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge) {
        super(name, shortName);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public BroadlinkBridge getBroadlinkBridge() {
        return broadlinkBridge;
    }

    @Override
    public CommandResult sendInfraredCommand(String deviceName, String commandName) {
        return null;
    }
}
