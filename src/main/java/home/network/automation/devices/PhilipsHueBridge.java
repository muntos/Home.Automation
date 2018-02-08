package home.network.automation.devices;

import home.network.automation.model.HueBridgeErrorResponse;
import home.network.automation.model.HueLight;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
public class PhilipsHueBridge extends Device{
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

    public PhilipsHueBridge(String name, String shortName, String address, String user){
        super(name, shortName);
        this.address = address;
        this.user = user;
        this.protocol = "http";
        this.port = 80;
    }

    private UriComponents buildURI(String path, Map<String, String> queryParams){
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

    public HueLight getLight(int id){
        log.info("Get light status for id={}", id);
        Map<String, String> queryParams = new HashMap<>();
        String path = "api" +"/" + user + "/" + "lights" + "/" + String.valueOf(id);
        String url = buildURI(path, queryParams).toUriString();
        HueLight light = null;
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory rf =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(READ_TIMEOUT);
        rf.setConnectTimeout(CONNECT_TIMEOUT);
        try {
            ResponseEntity<HueLight> response = restTemplate
                    .getForEntity(url, HueLight.class);
            light = response.getBody();
        } catch (HttpMessageNotReadableException ex)   {
            ParameterizedTypeReference<List<HueBridgeErrorResponse>> list = new ParameterizedTypeReference<List<HueBridgeErrorResponse>>() {};
            ResponseEntity<List<HueBridgeErrorResponse>> response = restTemplate.exchange(url, HttpMethod.GET,null, list);
            List<HueBridgeErrorResponse> errors = response.getBody();
            log.error("Get light status for id={} returned error: {}", id, errors.get(0).getError().getDescription());
        } catch (RestClientException ex){
            log.error("Get light status for id={} returned exception: {}", id, ex.getMessage());
        }

        return light;
    }

}
