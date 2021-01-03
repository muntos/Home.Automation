package home.network.automation.devices.A1Sensor;

public enum A1SensorAir {
    AIR_QUALITY_EXCELLENT ((byte)0x00),
    AIR_QUALITY_GOOD ((byte)0x01),
    AIR_QUALITY_NORMAL ((byte)0x02),
    AIR_QUALITY_BAD((byte)0x03);

    private byte air;

    A1SensorAir(byte air) {
        this.air = air;
    }

    public byte getAir() {
        return air;
    }

    public static A1SensorAir getByValue(byte value) {
        A1SensorAir[] sensorAirs = A1SensorAir.values();
        for (A1SensorAir a1SensorAir: sensorAirs) {
            if (a1SensorAir.getAir() == value) {
                return a1SensorAir;
            }
        }
        return null;
    }
}
