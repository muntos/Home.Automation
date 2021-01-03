package home.network.automation.devices.broadlink.A1Sensor;

public enum A1SensorNoise {
    NOISE_QUIET ((byte)0x00),
    NOISE_NORMAL ((byte)0x01),
    NOISE_HIGH ((byte)0x02),
    NOISE_EXTREME ((byte)0x03);

    private byte noise;

    A1SensorNoise(byte noise) {
        this.noise = noise;
    }

    public byte getNoise() {
        return noise;
    }

    public static A1SensorNoise getByValue(byte value) {
        A1SensorNoise[] sensorNoises = A1SensorNoise.values();
        for (A1SensorNoise a1SensorNoise: sensorNoises) {
            if (a1SensorNoise.getNoise() == value) {
                return a1SensorNoise;
            }
        }
        return null;
    }
}
