package home.network.automation.devices.api;

import home.network.automation.model.philipsHue.HueBridgeErrorResponse;
import home.network.automation.model.philipsHue.HueBridgeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HueApiClient extends Api{
    private static final String API_PATH = "api";
    private final int READ_TIMEOUT = 2000;
    private final int CONNECT_TIMEOUT = 2000;

    private String username;

    private RestTemplate restTemplate;

    public HueApiClient(String protocol, String address, Integer port, String username) {
        super(protocol, address, port);
        this.username = username;
        buildRestTemplate();
    }

    private void buildRestTemplate() {
        restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory rf =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(READ_TIMEOUT);
        rf.setConnectTimeout(CONNECT_TIMEOUT);
    }

    @Retryable(value = {HueBridgeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public <T> T get(String path, Class<T> type) throws HueBridgeException {
        T obj = null;

        try {
            ResponseEntity<T> response = restTemplate
                    .getForEntity(buildURL(path), type);
            obj = response.getBody();
        } catch (HttpMessageNotReadableException ex)   {
            request(path, HttpMethod.GET, null);
        } catch (RestClientException ex){
            log.error("GET for {} returned exception: {}", type, ex.getMessage());
            throw new HueBridgeException("Error: " + ex.getMessage());
        }

        return obj;
    }

    private void request(String path, HttpMethod get, HttpEntity<Object> o) {
        ParameterizedTypeReference<List<HueBridgeErrorResponse>> list = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<HueBridgeErrorResponse>> response = restTemplate.exchange(buildURL(path), get, o, list);
        List<HueBridgeErrorResponse> errors = response.getBody();

        checkForErrors(errors);
    }

    @Retryable(value = {HueBridgeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void put(String path, Object request) throws HueBridgeException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<Object>(request, headers);
            request(path, HttpMethod.PUT, entity);
        }
        catch (RestClientException ex){
            log.error("PUT for path {} and body {} returned exception: {}", path, request, ex.getMessage());
            throw new HueBridgeException("Error: " + ex.getMessage());
        }
    }

    @Recover
    public void recover(HueBridgeException ex, String path, Object request) {
        log.error("Exception trying to run PUT on path '{}' ! Error: {}", path, ex.getMessage());
    }

    @Recover
    public <T> T recover(HueBridgeException ex, String path, Class<T> type) throws HueBridgeException {
        log.error("Exception trying to run GET on path '{}' ! Error: {}", path, ex.getMessage());
        throw ex;
    }

    private String buildURL(String path) {
        Map<String, String> queryParams = new HashMap<>();
        String fullPath = API_PATH +"/" + username + "/" + path;
        String url = buildURI(fullPath, queryParams).toUriString();

        return url;
    }

    private void checkForErrors( List<HueBridgeErrorResponse> errors) throws HueBridgeException {
        StringBuilder message = new StringBuilder();
        errors.stream().forEach(hueBridgeErrorResponse -> {
            if (hueBridgeErrorResponse.getError() != null) {
                message.append(hueBridgeErrorResponse.getError().getDescription()).append(" >>>> ");
            }
        });
        if (message.toString().length() > 0) {
            throw new HueBridgeException(message.toString());
        }
    }
}
