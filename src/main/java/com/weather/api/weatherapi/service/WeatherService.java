package com.weather.api.weatherapi.service;


import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.utils.WeatherDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;



@Log4j2
@RequiredArgsConstructor
@Service
public class WeatherService {


    @Value("${openweathermap.api.key}")
    private String OPEN_WEATHER_MAP_API_KEY;

    @Value("${openweathermap.base.url}")
    private String OPEN_WEATHER_MAP_API_BASE_URL;


    @Cacheable(value = "weatherCache", keyGenerator = "keyGenerator")
    public SimplifiedWeatherData getWeatherDataByCoordinate(Coordinate coordinate) {

        String url = String.format(OPEN_WEATHER_MAP_API_BASE_URL, coordinate.getLatitude(), coordinate.getLongitude(), OPEN_WEATHER_MAP_API_KEY);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseJson = Objects.requireNonNull(response.body()).string();
                JSONObject jsonResponse = new JSONObject(responseJson);

                WeatherData weatherData = WeatherDataUtils.getWeatherData(jsonResponse);
                return WeatherDataUtils.convertToSimplifiedWeatherData(weatherData);

            } else {
                System.err.println("Request was not successful: " + response.code());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
