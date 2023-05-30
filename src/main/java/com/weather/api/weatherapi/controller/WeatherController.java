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


    @GetMapping("/weather")
    public ResponseEntity<SimplifiedWeatherData> getWeather() throws IOException, GeoIp2Exception{

        Pair<String, Coordinate> ipAddressCoordinatePair = locationService.retrieveCoordinateFromIpAddress();
        SimplifiedWeatherData simplifiedWeatherData = weatherService.getWeatherDataByCoordinate(ipAddressCoordinatePair.getLeft(), ipAddressCoordinatePair.getRight());

        log.info("We are successfully acquired the weather data for the IP address: "+ ipAddressCoordinatePair.getLeft());
        return ResponseEntity.ok(simplifiedWeatherData);
    }

    @GetMapping("/weather/coordinates")
    public ResponseEntity<List<WeatherDataDto>> getHistoricalWeatherByCoordinates(@RequestParam double latitude, @RequestParam double longitude) {

        List<WeatherDataDto> weatherDataList = weatherService.getHistoricalWeatherByCoordinates(latitude, longitude);
        log.info(String.format("We have acquired the historical weather data query for the latitude: %s and longitude: %s", latitude, longitude));

        return ResponseEntity.ok(weatherDataList);
    }

}
