package com.weather.api.weatherapi.utils;

import com.weather.api.weatherapi.controller.dto.*;
import org.json.JSONObject;



public class WeatherDataUtils {


    public static WeatherData getWeatherData(JSONObject jsonResponse) {

        JSONObject main = jsonResponse.getJSONObject("main");
        JSONObject coord = jsonResponse.getJSONObject("coord");

        return WeatherData.builder()
            .visibility(jsonResponse.getInt("visibility"))
            .temp(main.getDouble("temp"))
            .minTemperature(main.getDouble("temp_min"))
            .humidity(main.getInt("humidity"))
            .pressure(main.getInt("pressure"))
            .feelsLike(main.getDouble("feels_like"))
            .maxTemperature(main.getDouble("temp_max"))
            .country(jsonResponse.getJSONObject("sys").getString("country"))
            .name(jsonResponse.getString("name"))
            .latitude(coord.getDouble("lat"))
            .longitude(coord.getDouble("lon"))
            .windSpeed(jsonResponse.getJSONObject("wind").getDouble("speed"))
            .build();
    }

    public static SimplifiedWeatherData convertToSimplifiedWeatherData(WeatherData weatherData) {

        return SimplifiedWeatherData.builder()
            .visibility(weatherData.getVisibility())
            .temperature(kelvinToCelsius(weatherData.getTemp()))
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
