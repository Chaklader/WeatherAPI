package com.weather.api.weatherapi.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.weather.api.weatherapi.utils.IpAddressFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;


@Log4j2
@RequiredArgsConstructor
@Service
public class LocationService {


    @Value("${geolite2.city.database.location}")
    private String dbLocation;


    public Coordinate retrieveCoordinateFromIpAddress() throws IOException, GeoIp2Exception {

        File database = new File(dbLocation);
        DatabaseReader dbReader = new DatabaseReader.Builder(database)
            .build();

        InetAddress ipAddress = InetAddress.getByName(IpAddressFinder.getClientIpAddressIfServletRequestExist());
        CityResponse response = dbReader.city(ipAddress);

        return Coordinate.builder()
            .latitude(response.getLocation().getLatitude())
            .longitude(response.getLocation().getLongitude())
            .build();
    }
}
