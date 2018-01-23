package home.network.automation.model;

public enum SmartPlugStatus {
    ON,
    OFF,
    UNKNOWN;

    public static SmartPlugStatus value(String status){
        switch (status){
            case "0":
                return OFF;
            case "1":
                return ON;
            default:
                return UNKNOWN;
        }
    }
}
