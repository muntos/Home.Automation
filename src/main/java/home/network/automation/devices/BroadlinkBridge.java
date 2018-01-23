package home.network.automation.devices;

import home.network.automation.model.BroadlinkBridgeResponse;
import home.network.automation.model.SmartPlugResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class BroadlinkBridge {
    private String protocol;
    private String address;
    private Integer port;

    public BroadlinkBridge(String protocol, String address, Integer port) {
        this.protocol = protocol;
        this.address = address;
        this.port = port;
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

    public <T extends BroadlinkBridgeResponse> T getStatus(String name, String macAddress, Class<T> type){
        log.info("Get '{}' (MAC = {}) status...",name, macAddress);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("deviceMac", macAddress);
        String url = buildURI("status", queryParams).toUriString();
        T entity = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<T> response = restTemplate
                    .getForEntity(url, type);
            entity = response.getBody();
            if (!entity.getStatus().equals(SmartPlugResponse.status.ok)){
                log.error("Get '{}' (MAC = {}) status returned: {}", name, macAddress, entity.getMsg());
            }
        }catch (RestClientException ex){
            log.error("Get '{}' (MAC = {}) status returned exception: {}", name, macAddress, ex.getMessage());
        }

        return entity;
    }
}
