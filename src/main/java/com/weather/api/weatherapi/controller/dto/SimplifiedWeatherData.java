package com.weather.api.weatherapi.controller.dto;

import lombok.*;


/*
- temperature and feelsLike: degree Celsius (°C) (Open weather map
  API provides in Kelvin, and we convert it)
- Visibility: given in meters
- Humidity: Humidity is expressed as a percentage (%).
- Pressure: measured in hectopascals (hPa) or millibars (mb)
- Wind Speed: given in meters per second (m/s)
* */
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
