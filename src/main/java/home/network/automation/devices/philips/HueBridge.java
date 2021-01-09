package home.network.automation.devices.philips;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import home.network.automation.devices.api.HueApiClient;
import home.network.automation.devices.generic.Device;
import home.network.automation.model.philipsHue.HueAmbientLightSensor;
import home.network.automation.model.philipsHue.HueLight;
import home.network.automation.model.philipsHue.HueLightState;
import home.network.automation.model.philipsHue.HueMotionSensor;
import home.network.automation.model.philipsHue.HueSensor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class HueBridge extends Device {
    private static final String LIGHTS_PATH = "lights";
    private static final String STATE_PATH = "state";
    private static final String SENSORS_PATH = "sensors";

    private static final Map<String, Class> sensorMappings = ImmutableMap.of
                   ("ZLLPresence", HueMotionSensor.class,
                    "ZLLLightLevel", HueAmbientLightSensor.class
                   );

    private Map<String, HueSensor> sensors;
    private Map<String, HueLight> lights;
    private HueApiClient hueApiClient;

    public HueBridge(String name, String shortName, HueApiClient hueApiClient) {
        super(name, shortName);
        this.hueApiClient = hueApiClient;
    }

    public HueLight getLight(int id) {
        log.debug("Get light status for id={}", id);
        String path = LIGHTS_PATH + "/" + id;
        return hueApiClient.get(path, HueLight.class);
    }

    public Map<String, HueLight> getLights() {
        if (lights != null) {
            return lights;
        }
        lights = new HashMap<>();
        String path = LIGHTS_PATH;
        String jsonString = hueApiClient.get(path, String.class);
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            JsonElement jsonElement = entry.getValue();
                Gson gson = new Gson();
                HueLight hueLight = gson.fromJson(jsonElement, HueLight.class);
                hueLight.setId(Integer.valueOf(entry.getKey()));
                lights.put(hueLight.getName(), hueLight);
        }

        return lights;
    }

    public Boolean setLight(String name, HueLightState state) {
        HueLight light = getLights().get(name);
        if (light == null) {
            log.error("Couldn't find light named '{}' in Hue Bridge lights!", name);
            return false;
        }
        return setLight(light.getId(), state);
    }

    private Boolean setLight(int id, HueLightState state) {
        log.debug("Set light id={} state {}", id, state);
        String path = LIGHTS_PATH + "/" + id + "/" + STATE_PATH;
        hueApiClient.put(path, state);
        return true;
    }

    private Map<String, HueSensor> getSensors() {
        if (sensors != null) {
            return sensors;
        }
        sensors = new HashMap<>();
        String path = SENSORS_PATH;
        String jsonString = hueApiClient.get(path, String.class);
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            JsonElement jsonElement = entry.getValue();
            String sensorType = ((JsonObject) jsonElement).getAsJsonPrimitive("type").getAsString();
            if (sensorMappings.containsKey(sensorType)) {
                Gson gson = new Gson();
                HueSensor hueSensor = (HueSensor) gson.fromJson(jsonElement, sensorMappings.get(sensorType));
                hueSensor.setId(Integer.valueOf(entry.getKey()));
                sensors.put(hueSensor.getName(), hueSensor);
            }
        }

        return sensors;
    }

    public <T extends HueSensor> T getSensor(String sensorName, Class<T> type) {
        String path = SENSORS_PATH + "/" + getSensors().get(sensorName).getId();;
        return hueApiClient.get(path, type);
    }

}
