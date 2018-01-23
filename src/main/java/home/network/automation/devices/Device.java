package home.network.automation.devices;

public abstract class Device{
    protected String name;
    protected String shortName;

    public Device(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

}
