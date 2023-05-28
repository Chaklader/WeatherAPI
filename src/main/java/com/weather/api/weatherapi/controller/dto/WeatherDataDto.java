package com.weather.api.weatherapi.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class WeatherDataDto {

    private double currentTemperature;
    private double minTemperature;
    private double maxTemperature;
    private double feelsLike;

    private int humidity;
    private int pressure;
    private int visibility;
    private double windSpeed;

    private Date queryTimestamp;

    private String ipAddress;
}