package home.network.automation.devices.broadlink.A1Sensor;

import com.github.mob41.blapi.A1Device;
import com.github.mob41.blapi.EnvData;
import com.github.mob41.blapi.mac.Mac;
import home.network.automation.devices.generic.Device;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class A1Sensor extends Device {
    private String macAddress;
    private String ipAddress;

    public A1Sensor(String name, String shortName, String macAddress, String ipAddress){
        super(name, shortName);
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
    }

    public Optional<A1Response> getReadings() {
        try {
            A1Device dev = new A1Device(ipAddress, new Mac(macAddress));

            if (!dev.auth()) {
                log.error("Failed to authorize '{}' using MAC:{} and IP:{}", shortName, macAddress, ipAddress);
                return Optional.empty();
            }

            EnvData envData = dev.getSensorsData();
            if (envData == null) {
                log.error("Failed to get sensor data for '{}', received null value!", shortName);
                return Optional.empty();
            }

            A1Response response = new A1Response(envData);
            log.info(response.toString());

            return Optional.ofNullable(response);
        } catch (Exception ex) {
            log.error("Error trying to get A1 sensor data, error is: {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
