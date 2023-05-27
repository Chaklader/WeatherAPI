package com.weather.api.weatherapi.controller.dto;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedWeatherData {

    private int visibility;
    private double temperature;
    private int humidity;
    private int pressure;
    private double feelsLike;
    private double windSpeed;
}
