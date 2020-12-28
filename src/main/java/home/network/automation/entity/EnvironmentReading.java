package home.network.automation.entity;

import home.network.automation.model.Location;
import home.network.automation.model.Sensor;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "environment_reading")
public class EnvironmentReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Location location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sensor sensor;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public String getDateWithoutHour(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = simpleDateFormat.format(date);

        return sDate;
    }

    public String getDateWithoutDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String sDate = simpleDateFormat.format(date);

        return sDate;
    }

}
