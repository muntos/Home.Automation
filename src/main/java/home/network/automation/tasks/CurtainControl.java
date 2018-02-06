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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CurtainControl implements KodiListener {
    @Autowired
    private Kodi kodi;
    @Autowired
    private House house;
    @Autowired
    private CommandsService commandsService;

    private Map<String, ScheduledFuture> futures = new HashMap<>();

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String curtainLivingRoomName = "curtainLivingRoom";
    private RemoteControlledDevice curtainLivingRoom = house.getDevice(curtainLivingRoomName);

    @PostConstruct
    public void init(){
        if (curtainLivingRoom == null){
            log.error("Could not find any curtain named '{}', check your configuration!", curtainLivingRoomName);
            return;
        }
        kodi.addListener(this::eventReceived);
    }

    @Override
    public void eventReceived(Kodi.Event event) {
        log.info("Curtain control received Kodi event {}", event);
        controlLivingRoomCurtain(event);
    }

    private void controlLivingRoomCurtain(Kodi.Event event){
        Button curtainClose = curtainLivingRoom.getButton(Button.Mapping.CURTAIN_LIVINGROOM_CLOSE);
        Button curtainOpen = curtainLivingRoom.getButton(Button.Mapping.CURTAIN_LIVINGROOM_OPEN);

        switch (event){
            case PLAY_STARTED:
                scheduleCurtainAction(curtainLivingRoom, curtainClose, 60);
                break;
            case PLAY_PAUSED:
                break;
        }
    }

    private void scheduleCurtainAction(RemoteControlledDevice curtain, Button button, int delay){
        log.info("Schedule '{}' to {} in {} seconds", curtain.getName(), button.getFriendlyName(), delay);
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            commandsService.pressRemoteButton(curtain.getName(), button.getButtonName());
            futures.remove(curtain.getName());
        }, delay, TimeUnit.SECONDS);
        futures.put(curtain.getName(), future);
    }
}
