package home.network.automation;

import home.network.automation.devices.*;
import home.network.automation.model.Button;
import home.network.automation.observer.House;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${rmpro.mac}")
    private String rmProMac;

    @Value("${sp3.h80.mac}")
    private String sp3forH80Mac;

    @Value("${rm.bridge.protocol}")
    private String rmBridgeProtocol;

    @Value("${rm.bridge.address}")
    private String rmBridgeAddress;

    @Value("${rm.bridge.port}")
    private Integer rmBridgePort;

    @Value("${logitech.harmony.address}")
    private String harmonyAddress;

    @Bean
    House house(){
        BroadlinkBridge broadlinkBridge = new BroadlinkBridge(rmBridgeProtocol, rmBridgeAddress, rmBridgePort);
        House house = new House();
        house
            .addDevice(new SmartPlug("Broadlink SP3 connected to Hegel H80 amplifier","SP3_H80", sp3forH80Mac, broadlinkBridge))
            .addDevice(new BroadlinkHub("Broadlink RM-PRO", "RMPRO", rmProMac, broadlinkBridge))
            .addDevice(new HarmonyHub("Logitech Harmony Elite", "Harmony", harmonyAddress, 100))
            .addDevice(new AudioDevice("Hegel Amp", "H80", 30, true)
                                .setPrefferredRemote(house.getDevice("RMPRO"))
                                .addButton(new Button(4, "VolumeUp", "Volume Up").mapsTo(Button.Mapping.VOLUME_UP))
                                .addButton(new Button(3, "VolumeDown", "Volume Down").mapsTo(Button.Mapping.VOLUME_DOWN)))
            .addDevice(new RemoteControlledDevice("Electric curtain for Living room", "curtain", true)
                                .addButton(new Button(3, "close", "Curtain close"))
                                .addButton(new Button(4, "open", "Curtain open")))
            .addDevice(new RemoteControlledDevice("LG OLED TV", "TV")
                                .addButton(new Button(0, "chUp", "Channel Up"))
                                .addButton(new Button(0, "chDown", "Channel Down")));

        return house;
    }

    @Bean
    LogbackFilter logbackFilter(){
        return new LogbackFilter();
    }

    @Bean
    ApplicationContextProvider applicationContextProvider(){
        return new ApplicationContextProvider();
    }
}
