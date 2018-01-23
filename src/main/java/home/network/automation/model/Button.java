package home.network.automation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Button {
    private int buttonId;
    private String buttonName;
    private String friendlyName;

}
