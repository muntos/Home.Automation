package external.logitech.harmony;

import external.logitech.harmony.config.Activity;
import external.logitech.harmony.config.Activity.Status;

public abstract class ActivityStatusListener implements HarmonyHubListener {  
    public abstract void activityStatusChanged(Activity activity, Status status);

    @Override
    public void addTo(HarmonyClient harmonyClient) {
        harmonyClient.addListener(this);
    }

    @Override
    public void removeFrom(HarmonyClient harmonyClient) {
        harmonyClient.removeListener(this);
    }
}
