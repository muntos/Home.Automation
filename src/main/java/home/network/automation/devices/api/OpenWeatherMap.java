package home.network.automation.devices.api;

import home.network.automation.model.*;
import home.network.automation.model.OpenWeatherMap.OpenWeatherMapForecastList;
import home.network.automation.model.OpenWeatherMap.OpenWeatherMapForecastResponse;
import home.network.automation.model.OpenWeatherMap.OpenWeatherMapNowResponse;
import home.network.automation.model.OpenWeatherMap.OpenWeatherMapWeather;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class OpenWeatherMap extends Api{
    private final static String API_PATH = "/data/2.5/";

    @Value("${open.weather.map.api.key}")
    private String apiKey;

    @Value("#{${open.weather.map.api.cities}}")
    private Map<String,Long> cities;

    public OpenWeatherMap(@Value("${open.weather.map.api.protocol}") String protocol,
                          @Value("${open.weather.map.api.address}") String address,
                          @Value("${open.weather.map.api.port}") Integer port) {
        super(protocol, address, port);
    }

    public Optional<OpenWeatherMapNowResponse> getCurrentWeather(String city) {
        Map<String, OpenWeatherMapNowResponse> map = new HashMap<>();

        cities.forEach((key, value) -> {
            log.info("Get current weather for city {}", key);
            try {
                map.put(key, get(API_PATH + "weather", getQueryParams(value.toString()), OpenWeatherMapNowResponse.class));
            }catch (RestClientException ex){
                log.error("Get current weather returned error: ", ex.getMessage());
            }

        });

        return Optional.ofNullable(map.get(city));

    }

    public Optional<OpenWeatherMapForecastResponse> getForecastWeather(String city) {
        Map<String, OpenWeatherMapForecastResponse> map = new HashMap<>();

        cities.forEach((key, value) -> {
            try {
                map.put(key, get(API_PATH + "forecast", getQueryParams(value.toString()), OpenWeatherMapForecastResponse.class));
            }catch (RestClientException ex){
                log.error("Get forecast weather returned error: ", ex.getMessage());
            }

        });

        return Optional.ofNullable(map.get(city));
    }

    public Optional<ForecastWeather> isRainInForecast(String city, int numberOfDaysInFuture) {
        Optional<OpenWeatherMapForecastResponse> forecastWeather = getForecastWeather(city);

        if (forecastWeather.isPresent()) {
            DateTime dayForTheForecast;
            DateTimeZone timeZone = DateTimeZone.forID("Europe/" + city);
            if (numberOfDaysInFuture == 0) {
                dayForTheForecast = new DateTime(timeZone).plusDays(numberOfDaysInFuture);
            }
            else {
                dayForTheForecast = new DateTime(timeZone).withTimeAtStartOfDay().plusDays(numberOfDaysInFuture);
            }
            Interval intervalForTheForecast = new Interval( dayForTheForecast, dayForTheForecast.plusDays(1).withTimeAtStartOfDay() );

            for (OpenWeatherMapForecastList item : forecastWeather.get().getList()) {
                DateTime forecastedTime = new DateTime(item.getDt() * 1000).withZone(timeZone);
                if (intervalForTheForecast.contains(forecastedTime)) {
                    Optional<OpenWeatherMapWeather> weather = item.getWeather().stream()
                            .filter(x -> x.getId() >= 500 && x.getId() < 600)
                            .findFirst();
                    if (weather.isPresent()) {
                        return Optional.of(new ForecastWeather(weather.get().getMain(), weather.get().getDescription(), forecastedTime));
                    }
                }
            }
        }

        return Optional.empty();
    }

    private Map<String, String> getQueryParams(String id) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("id", id.toString());
        queryParams.put("units", "metric");
        queryParams.put("APPID", apiKey);

        return queryParams;
    }
}
