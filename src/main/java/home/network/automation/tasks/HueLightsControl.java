package home.network.automation.tasks;

import home.network.automation.components.Kodi;
import home.network.automation.components.KodiListener;
import home.network.automation.devices.philips.HueBridge;
import home.network.automation.model.philipsHue.HueLightState;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class HueLightsControl implements KodiListener {

    @Autowired
    private Kodi kodi;

    @Autowired
    private House house;

    @PostConstruct
    public void init(){
        kodi.addListener(this::eventReceived);
    }

    @Override
    public void eventReceived(Kodi.Event event) {
        log.info("Hue Lights control received Kodi event {}", event);
        controlLivingRoomLights(event);
    }

    private void controlLivingRoomLights(Kodi.Event event){
        HueBridge bridge = house.getDevice("hue");
        switch (event){
            case PLAY_PAUSED:
                HueLightState onState = new HueLightState();
                onState.setOn(true);
                onState.setBrightness(80);
                onState.setHue(0);
                onState.setSaturation(0);
                bridge.setLight(1, onState);
                bridge.setLight(2, onState);
                break;
            case PLAY_STARTED:
            case PLAY_RESUMED:
                HueLightState offState = new HueLightState();
                offState.setOn(false);
                bridge.setLight(1, offState);
                bridge.setLight(2, offState);
                break;
        }
    }
}
