package home.network.automation.tasks;

import home.network.automation.components.Kodi;
import home.network.automation.components.KodiListener;
import home.network.automation.devices.generic.ElectricCurtain;
import home.network.automation.devices.generic.RemoteControlledDevice;
import home.network.automation.model.Button;
import home.network.automation.observer.House;
import home.network.automation.service.CommandsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
public class ElectricCurtainControl implements KodiListener {

    @Autowired
    private Kodi kodi;

    @Autowired
    private House house;

    @Autowired
    private CommandsService commandsService;

    private Map<String, ScheduledFuture> futures = new HashMap<>();

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init(){
        kodi.addListener(this::eventReceived);
    }

    @Override
    public void eventReceived(Kodi.Event event) {
        if (event == Kodi.Event.PLAY_STARTED || event == Kodi.Event.PLAY_ENDED) {
            log.info("Curtain control received Kodi event {}", event);
            controlLivingRoomCurtain(event);
        }
    }

    private void controlLivingRoomCurtain(Kodi.Event event){
        String curtainName = "curtainLivingRoom";
        ElectricCurtain curtain = house.getDevice(curtainName);
        if (curtain == null){
            log.error("Could not find any curtain named '{}', check your configuration!", curtainName);
            return;
        }

        Button curtainClose = curtain.getButton(Button.Mapping.CURTAIN_LIVINGROOM_CLOSE);
        Button curtainOpen = curtain.getButton(Button.Mapping.CURTAIN_LIVINGROOM_OPEN);
        if (curtainClose == null || curtainOpen == null){
            log.error("Could not find mapped buttons for '{}' curtain control, check your configuration!", curtainName);
            return;
        }

        Future existingFuture = futures.get(curtain.getName());
        if (existingFuture != null){
            log.info("Found an existing schedule for '{}' curtain, cancelling now!", curtain.getName());
            existingFuture.cancel(false);
            futures.remove(curtain.getName());
        }

        switch (event){
            case PLAY_STARTED:
                scheduleCurtainAction(curtain, curtainClose, curtain.getWaitOnEventBeforeClose());
                break;
            case PLAY_ENDED:
                scheduleCurtainAction(curtain, curtainOpen, curtain.getWaitOnEventBeforeOpen());
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
