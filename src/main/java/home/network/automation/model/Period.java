package home.network.automation.model;

public enum Period {
    TODAY,
    WEEK,
    MONTH,
    YEAR,
    CUSTOM;


    public static Period of(String period){
        try {
            Period per = Period.valueOf(period.toUpperCase());
            return per;
        }
        catch (IllegalArgumentException ex){
            return Period.CUSTOM;
        }
    }
}
