package com.weather.api.weatherapi.service;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.utils.IpAddressFinder;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.setField;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LocationServiceTest {

    private LocationService locationService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        locationService = new LocationService();
        setField(locationService, "dbLocation", "src/main/resources/GeoLite2-City.mmdb");
        setField(locationService, "gridSize", 0.1);
    }

    @Test
    public void testRetrieveCoordinateFromIpAddress() throws IOException, GeoIp2Exception {
        String ipAddress = "103.150.26.242";
        double latitude = 23.7;
        double longitude = 90.4;

        try (MockedStatic<IpAddressFinder> utilities = Mockito.mockStatic(IpAddressFinder.class)) {
            utilities.when(IpAddressFinder::getClientIpAddressIfServletRequestExist).thenReturn("103.150.26.242");

            Pair<String, Coordinate> coordinatePair = locationService.retrieveCoordinateFromIpAddress();


            String ip = coordinatePair.getLeft();
            Coordinate coordinate = coordinatePair.getRight();

            assertEquals(ipAddress, ip);
            assertEquals(latitude, coordinate.getLatitude());
            assertEquals(longitude, coordinate.getLongitude());
        }
    }
}
