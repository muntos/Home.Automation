package home.network.automation.devices;

public class BroadlinkBridge {
    private String protocol;
    private String address;
    private Integer port;

    public BroadlinkBridge(String protocol, String address, Integer port) {
        this.protocol = protocol;
        this.address = address;
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }
}
