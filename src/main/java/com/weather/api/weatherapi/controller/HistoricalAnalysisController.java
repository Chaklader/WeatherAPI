package com.weather.api.weatherapi.controller;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.controller.dto.GeographyDto;
import com.weather.api.weatherapi.controller.dto.SimplifiedWeatherData;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.service.GeographyService;
import com.weather.api.weatherapi.service.LocationService;
import com.weather.api.weatherapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@SuppressWarnings("LineLength")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/api/history")
public class HistoricalAnalysisController {

    private final GeographyService geographyService;

    @GetMapping("/queries/ip/{ipAddress}")
    public ResponseEntity<List<GeographyDto>> getHistoricalQueriesByIp(@PathVariable String ipAddress) {

        List<GeographyDto> queries = geographyService.getHistoricalQueriesByIp(ipAddress);
        return ResponseEntity.ok(queries);
    }

}
