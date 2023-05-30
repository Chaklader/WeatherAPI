package com.weather.api.weatherapi.service;


import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.controller.dto.mapper.WeatherDataMapper;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import com.weather.api.weatherapi.dao.repository.WeatherRepository;
import com.weather.api.weatherapi.service.exception.WeatherDataRetrievalException;
import com.weather.api.weatherapi.utils.GeographicalWeatherDataUtils;
import com.weather.api.weatherapi.utils.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


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

//    @Autowired
    private OkHttpClient client;

    @Autowired
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Cacheable(value = "weatherCache", keyGenerator = Parameters.KEY_GENERATOR)
    public SimplifiedWeatherData getWeatherDataByCoordinate(String ipAddress, Coordinate coordinate) {

        String OPEN_WEATHER_MAP_QUERY_URL = String.format(OPEN_WEATHER_MAP_API_BASE_URL, coordinate.getLatitude(), coordinate.getLongitude(), OPEN_WEATHER_MAP_API_KEY);

        CompletableFuture<SimplifiedWeatherData> future = new CompletableFuture<>();

        Request request = new Request.Builder().url(OPEN_WEATHER_MAP_QUERY_URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                handleOnResponse(response, ipAddress, future, coordinate);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleOnFailure(e, future, ipAddress, coordinate);
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("We can't retrieve the weather data from the external API and error message: "+ e.getMessage());
            return null;
        }
    }

    private void handleOnResponse(@NotNull Response response, String ipAddress, CompletableFuture<SimplifiedWeatherData> future, Coordinate coordinate) throws IOException {
        if (response.isSuccessful()) {
            String responseJson = Objects.requireNonNull(response.body()).string();
            JSONObject jsonResponse = new JSONObject(responseJson);

            Geography geography = GeographicalWeatherDataUtils.getWeatherData(jsonResponse);
            geography.setIpAddress(ipAddress);
            WeatherData weatherData = geography.getWeatherData();

            weatherRepository.save(weatherData);
            geographyRepository.save(geography);

            SimplifiedWeatherData simplifiedWeatherData = GeographicalWeatherDataUtils.convertToSimplifiedWeatherData(weatherData);
            future.complete(simplifiedWeatherData);
        } else {
            checkIfWeatherDataCanBeRetrieveFromDatabase(ipAddress, coordinate, future);
            log.warn("Request to the weather API was not successful with status code: " + response.code() + " and we are retrieving data from the database");
        }
    }

    private void handleOnFailure(@NotNull IOException e, CompletableFuture<SimplifiedWeatherData> future, String ipAddress, Coordinate coordinate) {
        checkIfWeatherDataCanBeRetrieveFromDatabase(ipAddress, coordinate, future);
        log.error("Request to the weather API was not successful with error message: " + e.getMessage() + " and we are retrieving data from the database");
    }

    private void checkIfWeatherDataCanBeRetrieveFromDatabase(String ipAddress, Coordinate coordinate, CompletableFuture<SimplifiedWeatherData> future) {
        retrieveWeatherDataFromDatabase(ipAddress, coordinate, future);
        if (!future.isDone()) {
            future.completeExceptionally(new WeatherDataRetrievalException(Parameters.USER_FRIENDLY_MESSAGE, Parameters.DETAILED_TECHNICAL_DESCRIPTION));
        }
    }

    private void retrieveWeatherDataFromDatabase(String ipAddress, Coordinate coordinate, CompletableFuture<SimplifiedWeatherData> future) {
        Optional<Geography> geographyOptional = geographyRepository.findFirstByIpAddressOrLatitudeAndLongitudeOrderByQueryTimestampDesc(ipAddress, coordinate.getLatitude(), coordinate.getLongitude());
        if (geographyOptional.isPresent()) {
            Geography existingGeography = geographyOptional.get();
            SimplifiedWeatherData simplifiedWeatherData = GeographicalWeatherDataUtils.convertToSimplifiedWeatherData(existingGeography.getWeatherData());
            future.complete(simplifiedWeatherData);
        }
    }


    public List<WeatherDataDto> getHistoricalWeatherByCoordinates(double latitude, double longitude) {
        List<WeatherData> andGeographyLongitude = weatherRepository.findAllByGeography_LatitudeAndGeography_Longitude(latitude, longitude);

        return andGeographyLongitude.stream().map(WeatherDataMapper::mapToDto).toList();
    }


}
