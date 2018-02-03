package home.network.automation.tasks;

import home.network.automation.components.Kodi;
import home.network.automation.components.KodiListener;
import home.network.automation.devices.RemoteControlledDevice;
import home.network.automation.model.Button;
import home.network.automation.observer.House;
import home.network.automation.service.CommandsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class CurtainControl implements KodiListener {
    @Autowired
    private Kodi kodi;
    @Autowired
    private House house;
    @Autowired
    private CommandsService commandsService;

    @PostConstruct
    public void init(){
        kodi.addListener(this::eventReceived);
    }

    @Override
    public void eventReceived(Kodi.Event event) {
        log.info("Curtain received Kodi event {}", event);
        String curtainName = "curtainLivingroom";
        RemoteControlledDevice curtain = house.getDevice(curtainName);
        if (curtain == null){
            log.error("Could not find any curtain named '{}', check your configuration!", curtainName);
            return;
        }

        Button curtainClose = curtain.getButton(Button.Mapping.CURTAIN_LIVINGROOM_CLOSE);
        commandsService.pressRemoteButton(curtainName, curtainClose.getButtonName());
    }
}
