package home.network.automation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommandResult {
    private Boolean success;
    private String message;
}
