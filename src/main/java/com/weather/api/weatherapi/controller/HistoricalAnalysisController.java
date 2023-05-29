package com.weather.api.weatherapi.controller;


import com.weather.api.weatherapi.controller.dto.GeographyDto;
import com.weather.api.weatherapi.service.GeographyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@SuppressWarnings("LineLength")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/api")
public class HistoricalAnalysisController {

    private final GeographyService geographyService;

    @GetMapping("/queries/ip/{ipAddress}")
    public ResponseEntity<List<GeographyDto>> getHistoricalQueriesByIp(@PathVariable String ipAddress) {

        List<GeographyDto> queries = geographyService.getHistoricalQueriesByIp(ipAddress);
        log.info("We have acquired the historical geographical data query for the IP address: "+ ipAddress);
        return ResponseEntity.ok(queries);
    }

}
