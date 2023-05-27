package com.weather.api.weatherapi.utils;

import com.weather.api.weatherapi.controller.dto.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherDataUtils {

    public static WeatherData getWeatherData(JSONObject jsonResponse) {

        final JSONObject main = jsonResponse.getJSONObject("main");
        final JSONObject system = jsonResponse.getJSONObject("sys");
        final JSONObject coordinates = jsonResponse.getJSONObject("coord");
        final JSONArray weather = jsonResponse.getJSONArray("weather");
        final JSONObject wind = jsonResponse.getJSONObject("wind");

        return WeatherData.builder()
            .temperature(main.getDouble("temp"))
            .tempMin(main.getDouble("temp_min"))
            .humidity(main.getInt("humidity"))
            .pressure(main.getInt("pressure"))
            .feelsLike(main.getDouble("feels_like"))
            .tempMax(main.getDouble("temp_max"))
            .visibility(jsonResponse.getInt("visibility"))
            .timezone(jsonResponse.getInt("timezone"))
            .clouds(Clouds.builder().all(jsonResponse.getJSONObject("clouds").getInt("all")).build())
            .sys(Sys.builder()
                .country(system.getString("country"))
                .sunrise(system.getLong("sunrise"))
                .sunset(system.getLong("sunset"))
                .id(system.getInt("id"))
                .type(system.getInt("type"))
                .build())
            .dt(jsonResponse.getLong("dt"))
            .coord(Coord.builder()
                .lon(coordinates.getDouble("lon"))
                .lat(coordinates.getDouble("lat"))
                .build())
            .weather(new Weather[]{
                Weather.builder()
                    .icon(weather.getJSONObject(0).getString("icon"))
                    .description(weather.getJSONObject(0).getString("description"))
                    .main(weather.getJSONObject(0).getString("main"))
                    .id(weather.getJSONObject(0).getInt("id"))
                    .build()
            })
            .name(jsonResponse.getString("name"))
            .cod(jsonResponse.getInt("cod"))
            .id(jsonResponse.getInt("id"))
            .base(jsonResponse.getString("base"))
            .wind(Wind.builder()
                .deg(wind.getInt("deg"))
                .speed(wind.getDouble("speed"))
                .build())
            .build();
    }

    public static SimplifiedWeatherData convertToSimplifiedWeatherData(WeatherData weatherData) {
        return SimplifiedWeatherData.builder()
            .visibility(weatherData.getVisibility())
            .temperature(kelvinToCelsius(weatherData.getTemperature()))
            .humidity(weatherData.getHumidity())
            .pressure(weatherData.getPressure())
            .feelsLike(kelvinToCelsius(weatherData.getFeelsLike()))
            .windSpeed(weatherData.getWind().getSpeed())
            .build();
    }

    private static double kelvinToCelsius(double temperatureInKelvin) {
        double temperatureInCelsius = temperatureInKelvin - 273.15;
        return Math.round(temperatureInCelsius * 100.0) / 100.0;
    }

}
