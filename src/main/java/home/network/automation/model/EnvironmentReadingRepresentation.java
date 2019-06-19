package home.network.automation.model;

import home.network.automation.entity.EnvironmentReading;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnvironmentReadingRepresentation extends EnvironmentReading {
    String label;

    @Builder
    public EnvironmentReadingRepresentation(EnvironmentReading reading, String label) {
        super(reading.getId(), reading.getLocation(), reading.getSensor(), reading.getValue(), reading.getDate());
        this.label = label;
    }
}
