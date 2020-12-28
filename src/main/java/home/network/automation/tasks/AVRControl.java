package home.network.automation.tasks;

import home.network.automation.components.Kodi;
import home.network.automation.components.KodiListener;
import home.network.automation.observer.House;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class AVRControl implements KodiListener {

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
        if (Kodi.Event.isPlaybackEvent(event)) {
            log.info("AVR control received Kodi event {}", event);
            setDisplayDim(event);
        }
    }

    private void setDisplayDim(Kodi.Event event){

        switch (event){
            case PLAY_STARTED:

            case PLAY_ENDED:

        }
    }

}
