package com.weather.api.weatherapi.utils;

import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import org.json.JSONObject;



public class WeatherDataUtils {


    public static WeatherData getWeatherData(JSONObject jsonResponse) {

        JSONObject main = jsonResponse.getJSONObject("main");
        JSONObject coord = jsonResponse.getJSONObject("coord");

        Geography geography = Geography.builder()
            .country(jsonResponse.getJSONObject("sys").getString("country"))
            .city(jsonResponse.getString("name"))
            .latitude(coord.getDouble("lat"))
            .longitude(coord.getDouble("lon"))
            .build();

        return WeatherData.builder()
            .visibility(jsonResponse.getInt("visibility"))
            .currentTemperature(main.getDouble("temp"))
            .minTemperature(main.getDouble("temp_min"))
            .maxTemperature(main.getDouble("temp_max"))
            .feelsLike(main.getDouble("feels_like"))
            .humidity(main.getInt("humidity"))
            .pressure(main.getInt("pressure"))
            .windSpeed(jsonResponse.getJSONObject("wind").getDouble("speed"))
            .geography(geography)
            .build();
    }

    public static SimplifiedWeatherData convertToSimplifiedWeatherData(WeatherData weatherData) {

        return SimplifiedWeatherData.builder()
            .visibility(weatherData.getVisibility())
            .temperature(kelvinToCelsius(weatherData.getCurrentTemperature()))
            .humidity(weatherData.getHumidity())
            .pressure(weatherData.getPressure())
            .feelsLike(kelvinToCelsius(weatherData.getFeelsLike()))
            .windSpeed(weatherData.getWindSpeed())
            .build();
    }

    private static double kelvinToCelsius(double temperatureInKelvin) {
        double temperatureInCelsius = temperatureInKelvin - 273.15;
        return Math.round(temperatureInCelsius * 100.0) / 100.0;
    }

}
