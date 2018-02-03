package home.network.automation.model;

import lombok.Getter;

import static home.network.automation.model.Button.Mapping.NO_MAPPING;

@Getter
public class Button {
    private int codeId;
    private String buttonName;
    private String friendlyName;
    private Mapping mapping = NO_MAPPING;

    public enum Mapping{
        NO_MAPPING,
        VOLUME_UP,
        VOLUME_DOWN,
        VOLUME_MUTE,
        CURTAIN_LIVINGROOM_OPEN,
        CURTAIN_LIVINGROOM_CLOSE,
        CURTAIN_LIVINGROOM_STOP
    }

    public Button(int codeId, String buttonName, String friendlyName) {
        this.codeId = codeId;
        this.buttonName = buttonName;
        this.friendlyName = friendlyName;
    }

    public Button mapsTo(Mapping mapping){
        this.mapping = mapping;
        return this;
    }

}
