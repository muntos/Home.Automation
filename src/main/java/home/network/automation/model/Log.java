package home.network.automation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Log {
    private String date;
    private String level;
    private String message;
}
