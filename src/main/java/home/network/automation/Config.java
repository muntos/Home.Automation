package home.network.automation;

import home.network.automation.devices.api.BroadlinkBridge;
import home.network.automation.devices.api.HueApiClient;
import home.network.automation.devices.broadlink.A1Sensor.A1Sensor;
import home.network.automation.devices.broadlink.A1Sensor.A1SensorLegacy;
import home.network.automation.devices.broadlink.RmProHub.BroadlinkHub;
import home.network.automation.devices.broadlink.Sp3Plug.SP3PlugLegacy;
import home.network.automation.devices.generic.AudioDevice;
import home.network.automation.devices.generic.ElectricCurtain;
import home.network.automation.devices.generic.NetworkAudioDevice;
import home.network.automation.devices.generic.RemoteControlledDevice;
import home.network.automation.devices.logitech.HarmonyHub;
import home.network.automation.devices.philips.HueBridge;
import home.network.automation.devices.tplink.TapoLogin;
import home.network.automation.devices.tplink.TapoP100Plug;
import home.network.automation.model.Button;
import home.network.automation.observer.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;

@Configuration
public class Config {

    @Bean
    HarmonyHub harmonyHub(@Value("${logitech.harmony.address}") String harmonyAddress,
                          @Value("${logitech.harmony.connect:false}") Boolean connectToHub) {
        return new HarmonyHub("Logitech Harmony Elite", "Harmony", harmonyAddress, 100, connectToHub);
    }

    @Bean
    HueBridge hueBridge(@Autowired HueApiClient hueApiClient) {
        return new HueBridge("Philips Hue Bridge", "Hue", hueApiClient);
    }

    @Bean
    HueApiClient hueApiClient(@Value("${philips.bridge.address}") String hueBridgeAddress, @Value("${philips.bridge.username}") String hueBridgeUserName) {
        return new HueApiClient("http", hueBridgeAddress, 80, hueBridgeUserName);
    }

    @Bean
    BroadlinkBridge broadlinkBridge(@Value("${broadlink.bridge.protocol}") String rmBridgeProtocol,
                                    @Value("${broadlink.bridge.address}") String rmBridgeAddress,
                                    @Value("${broadlink.bridge.port}") Integer rmBridgePort) {
        return new BroadlinkBridge(rmBridgeProtocol, rmBridgeAddress, rmBridgePort);
    }

    @Bean
    BroadlinkHub broadlinkHub(@Value("${broadlink.rmpro.module1.mac}") String rmProMac,
                              @Autowired BroadlinkBridge broadlinkBridge) {
        return new BroadlinkHub("Broadlink RM-PRO", "RMPRO", rmProMac, broadlinkBridge);
    }

    @Bean(name = "SP3_H80")
    SP3PlugLegacy sp3PlugLegacy1(@Value("${broadlink.sp3.module1.mac}") String sp3forH80Mac,
                                @Autowired BroadlinkBridge broadlinkBridge) {
        return new SP3PlugLegacy("Broadlink SP3 connected to Hegel H80 amplifier","SP3_H80", sp3forH80Mac, broadlinkBridge, 60, 60);
    }

    @Bean(name = "SP3_Window")
    SP3PlugLegacy sp3PlugLegacy2(@Value("${broadlink.sp3.module2.mac}") String sp3forWindow,
                                @Autowired BroadlinkBridge broadlinkBridge) {
        return new SP3PlugLegacy("Broadlink SP3 mini martor for window sensor", "SP3_Window", sp3forWindow, broadlinkBridge, 0, 0);
    }

    @Bean(name = "P100_H80")
    TapoP100Plug tapoP100PlugModule1(@Value("${tp-link.tapo.login.username}") String tapoLoginUsername,
                              @Value("${tp-link.tapo.login.password}") String tapoLoginPassword,
                              @Value("${tp-link.tapo.plugs.P100.module1.address}") String ipAddress) {
        return new TapoP100Plug("Tapo P100 connected to Hegel H80", "P100_H80", ipAddress, new TapoLogin(tapoLoginUsername, tapoLoginPassword), 60, 60);
    }

    @Bean(name = "P100_Rack_Vents_12V")
    TapoP100Plug tapoP100PlugModule3(@Value("${tp-link.tapo.login.username}") String tapoLoginUsername,
                              @Value("${tp-link.tapo.login.password}") String tapoLoginPassword,
                              @Value("${tp-link.tapo.plugs.P100.module3.address}") String ipAddress) {
        return new TapoP100Plug("Tapo P100 connected to kitchen rack vents 12V", "P100_Rack_Vents_12V", ipAddress, new TapoLogin(tapoLoginUsername, tapoLoginPassword), 60, 0);
    }

    @Bean(name = "P100_Rack_Vents_6V")
    TapoP100Plug tapoP100PlugModule4(@Value("${tp-link.tapo.login.username}") String tapoLoginUsername,
                                     @Value("${tp-link.tapo.login.password}") String tapoLoginPassword,
                                     @Value("${tp-link.tapo.plugs.P100.module4.address}") String ipAddress) {
        return new TapoP100Plug("Tapo P100 connected to kitchen rack vents 6V", "P100_Rack_Vents_6V", ipAddress, new TapoLogin(tapoLoginUsername, tapoLoginPassword), 60, 0);
    }

    @Bean
    A1Sensor a1Sensor(@Value("${broadlink.a1.module1.mac}") String a1Module1MacAddress,
                      @Value("${broadlink.a1.module1.address}") String a1Module1IpAddress) {
        return new A1Sensor("Broadlink A1 Sensor for kitchen rack", "A1Kitchen", a1Module1MacAddress, a1Module1IpAddress);
    }

    @Bean
    A1SensorLegacy a1SensorLegacy(@Value("${broadlink.a1.module1.mac}") String a1Module1MacAddress,
                                  @Autowired BroadlinkBridge broadlinkBridge) {
        return new A1SensorLegacy("Broadlink A1 Sensor in Livingroom Balcony", "A1_Balcony_Living", a1Module1MacAddress, broadlinkBridge);
    }

    @Bean(name = "HegelH80")
    AudioDevice audioDevice(@Autowired BroadlinkHub broadlinkHub) {
        AudioDevice audioDevice = new AudioDevice("Hegel Amp", "H80", 30, true);
        audioDevice.setPrefferredRemote(broadlinkHub)
                   .addButton(new Button(4, "VolumeUp", "Volume Up").mapsTo(Button.Mapping.VOLUME_UP))
                   .addButton(new Button(3, "VolumeDown", "Volume Down").mapsTo(Button.Mapping.VOLUME_DOWN));
        return audioDevice;
    }

    @Bean(name = "X4500H")
    NetworkAudioDevice networkAudioDevice(@Value("${denon.X4500H.address}") String address) {
        return new NetworkAudioDevice("Denon X4500H AVR", "X4500H", 35, false, address);
    }

    @Bean(name = "curtainLivingRoom")
    ElectricCurtain electricCurtain() {
        ElectricCurtain electricCurtain = new ElectricCurtain("Electric curtain for Living room", "curtainLivingRoom", 30, 20);
        electricCurtain
                .addButton(new Button(8, "close", "Curtain close").mapsTo(Button.Mapping.CURTAIN_LIVINGROOM_CLOSE))
                .addButton(new Button(11, "open", "Curtain open").mapsTo(Button.Mapping.CURTAIN_LIVINGROOM_OPEN))
                .addButton(new Button(10, "stop", "Curtain stop").mapsTo(Button.Mapping.CURTAIN_LIVINGROOM_STOP));
        return electricCurtain;
    }

    @Bean(name = "LG77C9")
    RemoteControlledDevice remoteControlledDevice() {
        return new RemoteControlledDevice("LG OLED TV", "LG77C9")
                .addButton(new Button(0, "chUp", "Channel Up"))
                .addButton(new Button(0, "chDown", "Channel Down"));
    }

    @Bean
    House house(@Autowired HarmonyHub harmonyHub,
                @Autowired HueBridge hueBridge,
                @Autowired BroadlinkHub broadlinkHub,
                @Autowired A1Sensor a1Sensor,
                @Autowired A1SensorLegacy a1SensorLegacy,
                @Named("P100_H80") TapoP100Plug tapoP100PlugModule1,
                @Named("P100_Rack_Vents_12V") TapoP100Plug tapoP100PlugModule3,
                @Named("P100_Rack_Vents_6V") TapoP100Plug tapoP100PlugModule4,
                @Named("SP3_H80") SP3PlugLegacy sp3PlugLegacy1,
                @Named("SP3_Window") SP3PlugLegacy sp3PlugLegacy2,
                @Named("HegelH80") AudioDevice hegelH80,
                @Named("X4500H") NetworkAudioDevice networkAudioDevice,
                @Named("curtainLivingRoom") ElectricCurtain electricCurtain,
                @Named("LG77C9") RemoteControlledDevice remoteControlledDevice) {

        House house = new House();
        house
            .addDevice(sp3PlugLegacy1)
            .addDevice(sp3PlugLegacy2)
            .addDevice(broadlinkHub)
            .addDevice(harmonyHub)
            .addDevice(hueBridge)
            .addDevice(hegelH80)
            .addDevice(networkAudioDevice)
            .addDevice(electricCurtain)
            .addDevice(remoteControlledDevice)
            .addDevice(a1SensorLegacy)
            .addDevice(a1Sensor)
            .addDevice(tapoP100PlugModule1)
            .addDevice(tapoP100PlugModule3)
            .addDevice(tapoP100PlugModule4);



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
