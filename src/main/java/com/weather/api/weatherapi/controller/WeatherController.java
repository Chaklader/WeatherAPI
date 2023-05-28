package com.weather.api.weatherapi.controller;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.service.LocationService;
import com.weather.api.weatherapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@SuppressWarnings("LineLength")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/api")
public class WeatherController {

    private final WeatherService weatherService;

    private final LocationService locationService;

    // TODO
    /*
        0. TODO: DB schema should allow a historical analysis of both queries from a
           specific IP and of weather conditions for specific coordinates
        1. Create base Exception handler and handle the exceptions accordingly
        2. provide good logging in the entire app
        3. TODO: Write unit and integration tests for the app (coverage: 80%)
        4. Add the postman collection with the project
        5. Provide the docker and docker-compose file
        6. TODO: make the client singleton
    * */
    @GetMapping("/weather")
    public ResponseEntity<SimplifiedWeatherData> getWeather() throws IOException, GeoIp2Exception {

        Pair<String, Coordinate> stringCoordinatePair = locationService.retrieveCoordinateFromIpAddress();

        SimplifiedWeatherData weatherDataByCoordinate = weatherService.getWeatherDataByCoordinate(stringCoordinatePair.getLeft(), stringCoordinatePair.getRight());
        return ResponseEntity.ok(weatherDataByCoordinate);
    }


    // TODO: why use @RequestParam in one controller and @PathVariable in another?
    @GetMapping("/weather/coordinates")
    public ResponseEntity<List<WeatherDataDto>> getHistoricalWeatherByCoordinates(@RequestParam double latitude, @RequestParam double longitude) {

        List<WeatherDataDto> weatherDataList = weatherService.getHistoricalWeatherByCoordinates(latitude, longitude);
        return ResponseEntity.ok(weatherDataList);
    }

}
