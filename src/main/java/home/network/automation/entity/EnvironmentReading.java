package home.network.automation.entity;

import home.network.automation.model.Location;
import home.network.automation.model.Sensor;
import lombok.*;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public LocalDate getDateWithoutHour(){
       return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
