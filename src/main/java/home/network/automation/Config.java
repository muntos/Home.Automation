package home.network.automation;

import home.network.automation.devices.BroadlinkBridge;
import home.network.automation.devices.SmartPlug;
import home.network.automation.observer.House;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${rmpro.mac}")
    private String rmProMac;

    @Value("${rm.bridge.protocol}")
    private String rmBridgeProtocol;

    @Value("${rm.bridge.address}")
    private String rmBridgeAddress;

    @Value("${rm.bridge.port}")
    private Integer rmBridgePort;

    @Bean
    House house(){
        BroadlinkBridge broadlinkBridge = new BroadlinkBridge(rmBridgeProtocol, rmBridgeAddress, rmBridgePort);
        House house = new House();
        house.addDevice(new SmartPlug("Broadlink SP3 connected to Hegel H80 amplifier","SP3_H80", rmProMac, broadlinkBridge));

        return house;
    }
}
