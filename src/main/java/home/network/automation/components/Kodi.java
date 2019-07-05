package home.network.automation.components;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Kodi {
    public enum Event{
        PLAY_STARTED,
        PLAY_PAUSED,
        PLAY_ENDED,
        PLAY_RESUMED;

        public static Event of(String ev){
            switch (ev.toLowerCase()){
                case "playstarted":
                    return PLAY_STARTED;
                case "playpaused":
                    return PLAY_PAUSED;
                case "playended":
                    return PLAY_ENDED;
                case "playresumed":
                    return PLAY_RESUMED;
                default:
                    return null;
            }
        }

        public static Boolean isPlaybackEvent(Event event) {
            return Arrays.asList(PLAY_STARTED, PLAY_PAUSED, PLAY_RESUMED, PLAY_ENDED).contains(event);
        }
    }

    private List<KodiListener> listeners = new ArrayList<>();

    public void addListener(KodiListener listener){
        listeners.add(listener);
    }

    public void eventReceived(Event event){
        for (KodiListener listener : listeners){
            listener.eventReceived(event);
        }
    }
}
