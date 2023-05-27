package com.weather.api.weatherapi.service;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@Getter
@Builder
@EqualsAndHashCode
public class Coordinate {

    private final Double latitude;

    private final Double longitude;
}
