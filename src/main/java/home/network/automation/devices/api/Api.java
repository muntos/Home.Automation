package home.network.automation.devices.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class Api {
    private final int READ_TIMEOUT = 3000;
    private final int CONNECT_TIMEOUT = 3000;

    private String protocol;
    private String address;
    private Integer port;

    protected UriComponents buildURI(String path, Map<String, String> queryParams){
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

    protected <T> T get(String path, Map<String, String> queryParams, Class<T> type) throws RestClientException{
        T entity;
        String url = buildURI(path, queryParams).toUriString();

        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory rf =
                (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();
        rf.setReadTimeout(READ_TIMEOUT);
        rf.setConnectTimeout(CONNECT_TIMEOUT);
        ResponseEntity<T> response = restTemplate
                .getForEntity(url, type);
        entity = response.getBody();

        return entity;

    }
}
