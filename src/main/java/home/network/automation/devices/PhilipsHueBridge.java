package home.network.automation.devices;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import home.network.automation.devices.generic.Device;
import home.network.automation.model.PhilipsHue.HueAmbientLightSensor;
import home.network.automation.model.PhilipsHue.HueBridgeErrorResponse;
import home.network.automation.model.PhilipsHue.HueLight;
import home.network.automation.model.PhilipsHue.HueLightState;
import home.network.automation.model.PhilipsHue.HueMotionSensor;
import home.network.automation.model.PhilipsHue.HueSensor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public class PhilipsHueBridge extends Device {
    private static final String API_PATH = "api";
    private static final String LIGHTS_PATH = "lights";
    private static final String STATE_PATH = "state";
    private static final String SENSORS_PATH = "sensors";

    private static final Map<String, Class> sensorMappings = ImmutableMap.of
                   ("ZLLPresence", HueMotionSensor.class,
                    "ZLLLightLevel", HueAmbientLightSensor.class
                   );

    private Map<String, HueSensor> sensors;

    private final int READ_TIMEOUT = 2000;
    private final int CONNECT_TIMEOUT = 2000;

    private String protocol;
    private String address;
    private Integer port;
    private String user;

    public PhilipsHueBridge(String name, String shortName, String protocol, String address, Integer port, String user) {
        super(name, shortName);
        this.protocol = protocol;
        this.address = address;
        this.port = port;
        this.user = user;
    }

    public PhilipsHueBridge(String name, String shortName, String address, String user) {
        super(name, shortName);
        this.address = address;
        this.user = user;
        this.protocol = "http";
        this.port = 80;
    }

    private UriComponents buildURI(String path, Map<String, String> queryParams) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            params.add(entry.getKey(), entry.getValue());
        }
        return UriComponentsBuilder
                .newInstance()
                .scheme(protocol)
                .host(address)
                .port(port)
                .path(path)
                .queryParams(params)
                .build();
    }

    private  <T> T get(String path, Class<T> type) {
        Map<String, String> queryParams = new HashMap<>();
        String fullPath = API_PATH +"/" + user + "/" + path;
        String url = buildURI(fullPath, queryParams).toUriString();
        T obj = null;
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory rf =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(READ_TIMEOUT);
        rf.setConnectTimeout(CONNECT_TIMEOUT);
        try {
            ResponseEntity<T> response = restTemplate
                    .getForEntity(url, type);
            obj = response.getBody();
        } catch (HttpMessageNotReadableException ex)   {
            ParameterizedTypeReference<List<HueBridgeErrorResponse>> list = new ParameterizedTypeReference<>() {};
            ResponseEntity<List<HueBridgeErrorResponse>> response = restTemplate.exchange(url, HttpMethod.GET,null, list);
            List<HueBridgeErrorResponse> errors = response.getBody();
            log.error("Get for {} returned error: {}", type, errors.get(0).getError().getDescription());
        } catch (RestClientException ex){
            log.error("Get for {} returned exception: {}", type, ex.getMessage());
        }

        return obj;
    }

    private String put(String path, Object request) {
        Map<String, String> queryParams = new HashMap<>();
        String fullPath = API_PATH +"/" + user + "/" + path;
        String url = buildURI(fullPath, queryParams).toUriString();
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory rf =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(READ_TIMEOUT);
        rf.setConnectTimeout(CONNECT_TIMEOUT);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<Object>(request, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            return response.getBody();
        }
        catch (RestClientException ex){
            log.error("Put for path {} and body {} returned exception: {}", path, request, ex.getMessage());
            return "Error: " + ex.getMessage();
        }
    }

    public HueLight getLight(int id) {
        log.debug("Get light status for id={}", id);
        String path = LIGHTS_PATH + "/" + id;
        return get(path, HueLight.class);
    }

    public String setLight(int id, HueLightState state) {
        log.debug("Set light id={} state {}", id, state);
        String path = LIGHTS_PATH + "/" + id + "/" + STATE_PATH;
        return put(path, state);
    }

    private Map<String, HueSensor> getSensors() {
        if (sensors != null) {
            return sensors;
        }
        sensors = new HashMap<>();
        String path = SENSORS_PATH;
        String jsonString = get(path, String.class);
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();

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
        return get(path, type);
    }

}
