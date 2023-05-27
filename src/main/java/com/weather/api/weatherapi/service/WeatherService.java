package com.weather.api.weatherapi.service;


import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.utils.IpAddressFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@Service
public class WeatherService {


    @Value("${openweathermap.api.key}")
    private String OPEN_WEATHER_MAP_API_KEY;

    @Value("${geolite2.city.database.location}")
    private String dbLocation;


    @Value("${openweathermap.base.url}")
    private String OPEN_WEATHER_MAP_API_BASE_URL;


    private Coordinate retrieveCoordinateFromIpAddress() throws IOException, GeoIp2Exception {

        File database = new File(dbLocation);
        DatabaseReader dbReader = new DatabaseReader.Builder(database)
            .build();

        InetAddress ipAddress = InetAddress.getByName(IpAddressFinder.getClientIpAddressIfServletRequestExist());
        CityResponse response = dbReader.city(ipAddress);

        return Coordinate.builder()
            .latitude(response.getLocation().getLatitude())
            .longitude(response.getLocation().getLongitude())
            .build();
    }


    public WeatherData getWeatherDataByCoordinate() throws IOException, GeoIp2Exception {

        Coordinate coordinate = retrieveCoordinateFromIpAddress();
        String url = String.format(OPEN_WEATHER_MAP_API_BASE_URL, coordinate.getLatitude(), coordinate.getLongitude(), OPEN_WEATHER_MAP_API_KEY);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseJson = Objects.requireNonNull(response.body()).string();
                JSONObject jsonResponse = new JSONObject(responseJson);

                return getWeatherData(jsonResponse);
            } else {
                System.err.println("Request was not successful: " + response.code());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static WeatherData getWeatherData(JSONObject jsonResponse) {

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


}
