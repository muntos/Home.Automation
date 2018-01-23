package home.network.automation.devices;

import home.network.automation.model.CommandResult;
import home.network.automation.model.SmartPlugResponse;
import home.network.automation.model.SmartPlugStatus;
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
public class SmartPlug extends Device {
    private SmartPlugStatus status;
    private String macAddress;
    private BroadlinkBridge broadlinkBridge;

    public SmartPlug(String name, String shortName, String macAddress, BroadlinkBridge broadlinkBridge) {
        super(name, shortName);
        this.macAddress = macAddress;
        this.broadlinkBridge = broadlinkBridge;
    }

    private UriComponents buildURI(String path, Map<String, String> queryParams){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            params.add(entry.getKey(), entry.getValue());
        }
        return UriComponentsBuilder
                .newInstance()
                .scheme(broadlinkBridge.getProtocol())
                .host(broadlinkBridge.getAddress())
                .port(broadlinkBridge.getPort())
                .path(path)
                .queryParams(params)
                .build();
    }

    public SmartPlugStatus getStatus() {
        log.info("Get Broadlink SP3 status (MAC = {})", macAddress);
        SmartPlugStatus status = SmartPlugStatus.UNKNOWN;
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("deviceMac", macAddress);
        String url = buildURI("status", queryParams).toUriString();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SmartPlugResponse> response = restTemplate
                    .getForEntity(url, SmartPlugResponse.class);
            SmartPlugResponse entity = response.getBody();
            if (entity.getStatus().equals(SmartPlugResponse.status.ok)){
                status = SmartPlugStatus.value(entity.getOnOffStatus());
            } else {
                log.error("Get {} status (MAC = {}) returned: {}", name, macAddress, entity.getMsg());
            }
        }catch (RestClientException ex){
            log.error("Get {} status (MAC = {}) returned exception: {}", name, macAddress, ex.getMessage());
        }

        log.info("{} (MAC = {}) status: {}", name, macAddress, status);
        this.status = status;
        return status;
    }

}
