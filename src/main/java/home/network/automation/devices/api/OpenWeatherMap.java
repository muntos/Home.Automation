package home.network.automation.devices.api;

import home.network.automation.model.OpenWeatherMapNowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class OpenWeatherMap extends Api{

    @Value("${open.weather.map.api.key}")
    private String apiKey;

    @Value("#{${open.weather.map.api.cities}}")
    private Map<String,Long> cities;

    public OpenWeatherMap(@Value("${open.weather.map.api.protocol}") String protocol,
                          @Value("${open.weather.map.api.address}") String address,
                          @Value("${open.weather.map.api.port}") Integer port) {
        super(protocol, address, port);
    }

    public Optional<OpenWeatherMapNowResponse> getCurrentWeather(String city){
        Map<String, OpenWeatherMapNowResponse> map = new HashMap<>();

        cities.forEach((key, value) -> {
            log.info("Get current weather for city {}", key);

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("id", value.toString());
            queryParams.put("units", "metric");
            queryParams.put("APPID", apiKey);

            try {
                map.put(key, get("/data/2.5/weather", queryParams, OpenWeatherMapNowResponse.class));
            }catch (RestClientException ex){
                log.error("Get current weather returned error: ", ex.getMessage());
            }

        });

        return Optional.ofNullable(map.get(city));

    }
}
