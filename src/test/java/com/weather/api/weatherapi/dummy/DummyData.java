package com.weather.api.weatherapi.dummy;

import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;

import java.util.Date;

public class DummyData {


    public static Geography getGeography(WeatherData weatherData) {
        return Geography.builder()
            .country("BD")
            .city("Dhaka")
            .latitude(23.7)
            .longitude(90.4)
            .ipAddress("103.150.26.242")
            .queryTimestamp(new Date())
            .weatherData(weatherData)
            .build();
    }

    public static WeatherData getWeatherData() {
        return WeatherData.builder()
            .currentTemperature(304.18)
            .feelsLike(306.02)
            .minTemperature(304.18)
            .maxTemperature(304.18)
            .pressure(1006)
            .humidity(51)
            .visibility(4000)
            .windSpeed(3.09)
            .queryTimestamp(new Date())
            .build();
    }

    public static Coordinate getCoordinate() {
        return Coordinate.builder().latitude(23.7).longitude(90.4).build();
    }
}
