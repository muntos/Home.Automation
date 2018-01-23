package home.network.automation.model;

import lombok.Getter;

import static home.network.automation.model.Button.Mapping.NO_MAPPING;

@Getter
public class Button {
    private int buttonId;
    private String buttonName;
    private String friendlyName;
    private Mapping mapping = NO_MAPPING;

    public enum Mapping{
        NO_MAPPING,
        VOLUME_UP,
        VOLUME_DOWN,
        VOLUME_MUTE
    }

    public Button(int buttonId, String buttonName, String friendlyName) {
        this.buttonId = buttonId;
        this.buttonName = buttonName;
        this.friendlyName = friendlyName;
    }

    public Button mapsTo(Mapping mapping){
        this.mapping = mapping;
        return this;
    }

}
