package home.network.automation.devices.A1Sensor;

public enum A1SensorLight {
    LIGHT_DARK ((byte)0x00),
    LIGHT_DIM ((byte)0x01),
    LIGHT_NORMAL ((byte)0x02),
    LIGHT_BRIGHT((byte)0x03);

    private byte light;

    A1SensorLight(byte light) {
        this.light = light;
    }

    public byte getLight() {
        return light;
    }

    public static A1SensorLight getByValue(byte value) {
        A1SensorLight[] sensorLights = A1SensorLight.values();
        for (A1SensorLight a1SensorLight: sensorLights) {
            if (a1SensorLight.getLight() == value) {
                return a1SensorLight;
            }
        }
        return null;
    }
}
