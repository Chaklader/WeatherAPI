package com.weather.api.weatherapi.controller.dto.mapper;

import com.weather.api.weatherapi.controller.dto.WeatherDataDto;
import com.weather.api.weatherapi.dao.model.WeatherData;

public class WeatherDataMapper {

    public static WeatherDataDto mapToDto(WeatherData weatherData) {
        return WeatherDataDto.builder()
                .currentTemperature(weatherData.getCurrentTemperature())
                .minTemperature(weatherData.getMinTemperature())
                .maxTemperature(weatherData.getMaxTemperature())
                .feelsLike(weatherData.getFeelsLike())
                .humidity(weatherData.getHumidity())
                .pressure(weatherData.getPressure())
                .visibility(weatherData.getVisibility())
                .windSpeed(weatherData.getWindSpeed())
                .queryTimestamp(weatherData.getQueryTimestamp())
                .ipAddress(weatherData.getGeography().getIpAddress())
                .build();
    }

}
