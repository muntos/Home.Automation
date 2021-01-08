package home.network.automation.devices.api;

import home.network.automation.devices.broadlink.RMPlugin.BroadlinkBridgeResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class BroadlinkBridge extends Api {

    public BroadlinkBridge(String protocol, String address, Integer port) {
        super(protocol, address, port);
    }

    public <T extends BroadlinkBridgeResponse> T getStatus(String name, String macAddress, String path, Class<T> type){
        log.info("Get '{}' (MAC = {}) status...",name, macAddress);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("deviceMac", macAddress);
        T entity = null;

        try {
            entity = get(path, queryParams, type);
            if (!entity.getStatus().equals(BroadlinkBridgeResponse.status.ok)){
                log.error("Get '{}' (MAC = {}) status returned: {}", name, macAddress, entity.getMsg());
            }
        }catch (RestClientException ex){
            log.error("Get '{}' (MAC = {}) status returned exception: {}", name, macAddress, ex.getMessage());
        }

        return entity;
    }

    public <T extends BroadlinkBridgeResponse> T sendCommand(Map<String, String> values, String name, String macAddress, Class<T> type){
        log.info("Send to '{}' (MAC = {}) command '{}'",name, macAddress, values);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("deviceMac", macAddress);
        queryParams.putAll(values);
        T entity = null;

        try {
            entity = get("send", queryParams, type);
            if (!entity.getStatus().equals(BroadlinkBridgeResponse.status.ok)){
                log.warn("Send to '{}' (MAC = {}) command '{}' returned: {}", name, macAddress, values, entity.getMsg());
            }
        }catch (RestClientException ex){
            log.warn("Send to '{}' (MAC = {}) command '{}' returned exception: {}", name, macAddress, values, ex.getMessage());
        }

        return entity;
    }
}
