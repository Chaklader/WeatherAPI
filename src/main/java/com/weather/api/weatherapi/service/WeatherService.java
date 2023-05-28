package com.weather.api.weatherapi.service;


import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import com.weather.api.weatherapi.dao.repository.WeatherRepository;
import com.weather.api.weatherapi.utils.GeographicalWeatherDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Service
public class WeatherService {


    @Value("${openweathermap.api.key}")
    private String OPEN_WEATHER_MAP_API_KEY;

    @Value("${openweathermap.base.url}")
    private String OPEN_WEATHER_MAP_API_BASE_URL;


    private final WeatherRepository weatherRepository;
    private final GeographyRepository geographyRepository;


    // TODO: make sure we can perform historical search - at the moment if the data exists, we just update it
    @Cacheable(value = "weatherCache", keyGenerator = "keyGenerator")
    public SimplifiedWeatherData getWeatherDataByCoordinate(String ipAddress, Coordinate coordinate) {

        String OPEN_WEATHER_MAP_QUERY_URL = String.format(OPEN_WEATHER_MAP_API_BASE_URL, coordinate.getLatitude(), coordinate.getLongitude(), OPEN_WEATHER_MAP_API_KEY);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(OPEN_WEATHER_MAP_QUERY_URL)
            .build();

        try (Response response = client.newCall(request).execute()) {

            Optional<Geography> geographyOptional = geographyRepository.findByIpAddressOrLatitudeAndLongitude(ipAddress, coordinate.getLatitude(), coordinate.getLongitude());

            if (response.isSuccessful()) {

                String responseJson = Objects.requireNonNull(response.body()).string();
                JSONObject jsonResponse = new JSONObject(responseJson);

                Geography geography = GeographicalWeatherDataUtils.getWeatherData(jsonResponse);
                geography.setIpAddress(ipAddress);
                WeatherData weatherData = geography.getWeatherData();

                if (geographyOptional.isPresent()) {
                    Geography existingGeography = geographyOptional.get();
                    WeatherData existingWeatherData = existingGeography.getWeatherData();

                    existingWeatherData.setCurrentTemperature(weatherData.getCurrentTemperature());
                    existingWeatherData.setMinTemperature(weatherData.getMinTemperature());
                    existingWeatherData.setMaxTemperature(weatherData.getMaxTemperature());
                    existingWeatherData.setFeelsLike(weatherData.getFeelsLike());
                    existingWeatherData.setHumidity(weatherData.getHumidity());
                    existingWeatherData.setPressure(weatherData.getPressure());
                    existingWeatherData.setVisibility(weatherData.getVisibility());
                    existingWeatherData.setWindSpeed(weatherData.getWindSpeed());

                    weatherRepository.save(existingWeatherData);

                    existingGeography.setCountry(geography.getCountry());
                    existingGeography.setCity(geography.getCity());
                    existingGeography.setLatitude(geography.getLatitude());
                    existingGeography.setLongitude(geography.getLongitude());
                    existingGeography.setIpAddress(geography.getIpAddress());

                    geographyRepository.save(existingGeography);

                } else {
                    weatherRepository.save(weatherData);
                    geographyRepository.save(geography);
                }

                return GeographicalWeatherDataUtils.convertToSimplifiedWeatherData(weatherData);

            } else {

                if (geographyOptional.isPresent()) {

                    Geography existingGeography = geographyOptional.get();
                    WeatherData existingWeatherData = existingGeography.getWeatherData();

                    return GeographicalWeatherDataUtils.convertToSimplifiedWeatherData(existingWeatherData);
                } else {
                    System.err.println("Request was not successful: " + response.code());
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // TODO: round the coordinates before the search
    public List<WeatherDataDto> getHistoricalWeatherByCoordinates(double latitude, double longitude) {

        List<WeatherData> andGeographyLongitude = weatherRepository.findAllByGeography_LatitudeAndGeography_Longitude(latitude, longitude);

        return andGeographyLongitude.stream().map(WeatherDataMapper::mapToDto).toList();
    }


}
