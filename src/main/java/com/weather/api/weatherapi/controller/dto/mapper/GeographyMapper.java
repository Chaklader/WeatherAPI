package com.weather.api.weatherapi.controller.dto.mapper;

import com.weather.api.weatherapi.controller.dto.GeographyDto;
import com.weather.api.weatherapi.dao.model.Geography;

public class GeographyMapper {

    public static GeographyDto mapToDto(Geography geography) {
        return GeographyDto.builder()
                .country(geography.getCountry())
                .city(geography.getCity())
                .latitude(geography.getLatitude())
                .longitude(geography.getLongitude())
                .ipAddress(geography.getIpAddress())
                .queryTimestamp(geography.getQueryTimestamp())
                .build();
    }
}