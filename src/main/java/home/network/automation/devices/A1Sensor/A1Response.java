package home.network.automation.devices.A1Sensor;

import com.github.mob41.blapi.EnvData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class A1Response {
    private Double temperature;
    private A1SensorLight light;
    private A1SensorNoise noisy;
    private Double humidity;
    private A1SensorAir air;

    public A1Response(EnvData envData) {
        this.temperature = Math.round(Double.valueOf(envData.getTemp()) * 100.0) / 100.0;
        this.humidity = Math.round(Double.valueOf(envData.getHumidity()) * 100.0) / 100.0;
        this.light = A1SensorLight.getByValue(envData.getLight());
        this.noisy = A1SensorNoise.getByValue(envData.getNoise());
        this.air = A1SensorAir.getByValue(envData.getAirQuality());
    }
}
