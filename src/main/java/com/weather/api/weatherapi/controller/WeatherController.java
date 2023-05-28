package com.weather.api.weatherapi.controller;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.service.LocationService;
import com.weather.api.weatherapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;


@SuppressWarnings("LineLength")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/api")
public class WeatherController {



    private final WeatherService weatherService;

    private final LocationService locationService;

    /*
        1. Create base Exception handler and handle the exceptions accordingly
        2. Implemented web service should be resilient to 3rd party service unavailability
        3. Data from 3rd party providers should be stored in a database and use database versioning
    * */

    @GetMapping("/weather")
    public ResponseEntity<SimplifiedWeatherData> getWeather() throws IOException, GeoIp2Exception {

        Pair<String, Coordinate> stringCoordinatePair = locationService.retrieveCoordinateFromIpAddress();

        SimplifiedWeatherData weatherDataByCoordinate = weatherService.getWeatherDataByCoordinate(stringCoordinatePair.getLeft(), stringCoordinatePair.getRight());
        return ResponseEntity.ok(weatherDataByCoordinate);
    }

}
