package com.weather.api.weatherapi.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.utils.GridBasedLocationUtils;
import com.weather.api.weatherapi.utils.IpAddressFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;


@Log4j2
@RequiredArgsConstructor
@Service
public class LocationService {

    private final GridBasedLocationUtils gridBasedLocationUtils;

    @Value("${geolite2.city.database.location}")
    private String dbLocation;

    public Pair<String, Coordinate> retrieveCoordinateFromIpAddress() throws IOException, GeoIp2Exception {

        File database = new File(dbLocation);
        DatabaseReader dbReader = new DatabaseReader.Builder(database)
            .build();

        InetAddress ipAddress = InetAddress.getByName(IpAddressFinder.getClientIpAddressIfServletRequestExist());

        String hostAddress = ipAddress.getHostAddress();
        CityResponse response = dbReader.city(ipAddress);

        return Pair.of(hostAddress, Coordinate.builder()
            .latitude(gridBasedLocationUtils.roundToGrid(response.getLocation().getLatitude()))
            .longitude(gridBasedLocationUtils.roundToGrid(response.getLocation().getLongitude()))
            .build());
    }

}
