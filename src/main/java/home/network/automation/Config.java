package home.network.automation;

import home.network.automation.devices.*;
import home.network.automation.devices.api.BroadlinkBridge;
import home.network.automation.devices.broadlink.A1Sensor.A1Sensor;
import home.network.automation.devices.broadlink.A1Sensor.A1SensorLegacy;
import home.network.automation.devices.broadlink.BroadlinkHub;
import home.network.automation.devices.broadlink.Sp3Plug.SP3Plug;
import home.network.automation.devices.tplink.TapoLogin;
import home.network.automation.devices.tplink.TapoP100Plug;
import home.network.automation.model.Button;
import home.network.automation.observer.House;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${broadlink.rmpro.module1.mac}")
    private String rmProMac;

    @Value("${broadlink.sp3.module1.mac}")
    private String sp3forH80Mac;

    @Value("${broadlink.sp3.module2.mac}")
    private String sp3forWindow;

    @Value("${broadlink.bridge.protocol}")
    private String rmBridgeProtocol;

    @Value("${broadlink.bridge.address}")
    private String rmBridgeAddress;

    @Value("${broadlink.bridge.port}")
    private Integer rmBridgePort;

    @Value("${logitech.harmony.address}")
    private String harmonyAddress;

    @Value("${logitech.harmony.connect:false}")
    private Boolean connectToHub;

    @Value("${philips.bridge.address}")
    private String hueBridgeAddress;

    @Value("${philips.bridge.username}")
    private String hueBridgeUserName;

    @Value("${broadlink.a1.module1.mac}")
    private String a1Module1MacAddress;

    @Value("${broadlink.a1.module1.address}")
    private String a1Module1IpAddress;

    @Value("${denon.X4500H.server}")
    private String denonTelnetPort;

    @Value("${tplink.tapo.login.username}")
    private String tapoLoginUsername;

    @Value("${tplink.tapo.login.password}")
    private String tapoLoginPassword;

    @Bean
    House house(){
        BroadlinkBridge broadlinkBridge = new BroadlinkBridge(rmBridgeProtocol, rmBridgeAddress, rmBridgePort);
        House house = new House();
        house
            .addDevice(new SP3Plug("Broadlink SP3 connected to Hegel H80 amplifier","SP3_H80", sp3forH80Mac, broadlinkBridge, 60, 60))
            .addDevice(new SP3Plug("Broadlink SP3 mini martor for window sensor", "SP3_Window", sp3forWindow, broadlinkBridge, 0, 0))
            .addDevice(new BroadlinkHub("Broadlink RM-PRO", "RMPRO", rmProMac, broadlinkBridge))
            .addDevice(new HarmonyHub("Logitech Harmony Elite", "Harmony", harmonyAddress, 100, connectToHub))
            .addDevice(new PhilipsHueBridge("Philips Hue Bridge", "Hue", hueBridgeAddress, hueBridgeUserName))
            .addDevice(new AudioDevice("Hegel Amp", "H80", 30, true)
                                .setPrefferredRemote(house.getDevice("RMPRO"))
                                .addButton(new Button(4, "VolumeUp", "Volume Up").mapsTo(Button.Mapping.VOLUME_UP))
                                .addButton(new Button(3, "VolumeDown", "Volume Down").mapsTo(Button.Mapping.VOLUME_DOWN)))
            .addDevice(new NetworkAudioDevice("Denon X4500H AVR", "X4500H", 35, false, denonTelnetPort))
            .addDevice(new ElectricCurtain("Electric curtain for Living room", "curtainLivingRoom", 30, 20)
                                .addButton(new Button(8, "close", "Curtain close").mapsTo(Button.Mapping.CURTAIN_LIVINGROOM_CLOSE))
                                .addButton(new Button(11, "open", "Curtain open").mapsTo(Button.Mapping.CURTAIN_LIVINGROOM_OPEN))
                                .addButton(new Button(10, "stop", "Curtain stop").mapsTo(Button.Mapping.CURTAIN_LIVINGROOM_STOP)))
            .addDevice(new RemoteControlledDevice("LG OLED TV", "TV")
                                .addButton(new Button(0, "chUp", "Channel Up"))
                                .addButton(new Button(0, "chDown", "Channel Down")))
            .addDevice(new A1SensorLegacy("Broadlink A1 Sensor in Livingroom Balcony", "A1_Balcony_Living", a1Module1MacAddress, broadlinkBridge))
            .addDevice(new A1Sensor("Broadlink A1 Sensor for kitchen rack", "A1Kitchen", a1Module1MacAddress, a1Module1IpAddress))
            .addDevice(new TapoP100Plug("Tapo P100 connected to Hegel H80", "P100_H80", "192.168.1.110", new TapoLogin(tapoLoginUsername, tapoLoginPassword), 60, 60));


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
