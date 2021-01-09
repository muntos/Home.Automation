package home.network.automation.tasks;

import home.network.automation.PhilipsHueLightsProps;
import home.network.automation.components.Kodi;
import home.network.automation.components.KodiListener;
import home.network.automation.devices.philips.HueBridge;
import home.network.automation.model.philipsHue.HueLightState;
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
    private PhilipsHueLightsProps lightsProps;

    @Autowired
    private HueBridge bridge;

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
        switch (event){
            case PLAY_PAUSED:
                lightsProps.getMultimediaControl().stream().forEach(name -> bridge.setLight(name, getHueLightStateForPlayPaused()));
                break;
            case PLAY_STARTED:
            case PLAY_RESUMED:
                HueLightState offState = new HueLightState();
                offState.setOn(false);
                lightsProps.getMultimediaControl().stream().forEach(name -> bridge.setLight(name, offState));
                break;
        }
    }

    private HueLightState getHueLightStateForPlayPaused() {
        HueLightState onState = new HueLightState();
        onState.setOn(true);
        onState.setBrightness(183);
        onState.setHue(46012);
        onState.setSaturation(254);
        onState.setXy(new Float[] {0.154f, 0.0807f});
        onState.setCt(153);

        return onState;
    }

}
