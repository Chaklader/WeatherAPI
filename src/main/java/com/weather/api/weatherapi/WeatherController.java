package com.weather.api.weatherapi;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;


@SuppressWarnings("LineLength")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/api")
public class WeatherController {

    private static final String[] IP_HEADER_CANDIDATES = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

    public static String getClientIpAddressIfServletRequestExist() {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ip;
            }
        }

        return request.getRemoteAddr();
    }


    @GetMapping("/weather")
    public ResponseEntity<Object> getWeather() {

        final String ipAddressIfServletRequestExist = getClientIpAddressIfServletRequestExist();
        return ResponseEntity.ok(null);
    }


    private static final String IP_ADDRESS = "103.150.26.242";

    private static final String OPEN_WEATHER_MAP_API_KEY = "6a2d7bcc0ccd7c6269addc6f1af23c8b";
    private static final String MAX_MIND_API_KEY = "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";


    public static void test() throws IOException, GeoIp2Exception {

        String dbLocation = "src/main/resources/GeoLite2-City.mmdb";

        File database = new File(dbLocation);
        DatabaseReader dbReader = new DatabaseReader.Builder(database)
            .build();

        InetAddress ipAddress = InetAddress.getByName(IP_ADDRESS);
        CityResponse response = dbReader.city(ipAddress);

        String countryName = response.getCountry().getName();
        String cityName = response.getCity().getName();

        final Double latitude = response.getLocation().getLatitude();
        final Double longitude = response.getLocation().getLongitude();

        getWeatherByCoordinates(latitude, longitude);
    }


    private static final String API_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";

    public static void getWeatherByCoordinates(double latitude, double longitude) {
        String url = String.format(API_BASE_URL, latitude, longitude, OPEN_WEATHER_MAP_API_KEY);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseJson = Objects.requireNonNull(response.body()).string();
                JSONObject jsonResponse = new JSONObject(responseJson);

                WeatherData weatherData = getWeatherData(jsonResponse);

                System.out.println("WeatherData: " + weatherData);
            } else {
                System.err.println("Request was not successful: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static WeatherData getWeatherData(JSONObject jsonResponse) {

        final JSONObject main = jsonResponse.getJSONObject("main");
        final JSONObject sys = jsonResponse.getJSONObject("sys");
        final JSONObject coord = jsonResponse.getJSONObject("coord");
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
                .country(sys.getString("country"))
                .sunrise(sys.getLong("sunrise"))
                .sunset(sys.getLong("sunset"))
                .id(sys.getInt("id"))
                .type(sys.getInt("type"))
                .build())
            .dt(jsonResponse.getLong("dt"))
            .coord(Coord.builder()
                .lon(coord.getDouble("lon"))
                .lat(coord.getDouble("lat"))
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


    public static void main(String[] args) throws IOException, GeoIp2Exception {
        try (WebServiceClient client = new WebServiceClient.Builder(42, MAX_MIND_API_KEY)
            .build()) {

            InetAddress ipAddress = InetAddress.getByName(IP_ADDRESS);

            CountryResponse response = client.country(ipAddress);

            Country country = response.getCountry();
            System.out.println(country.getIsoCode());            // 'US'
            System.out.println(country.getName());               // 'United States'
            System.out.println(country.getNames().get("zh-CN")); // '美国'
        }


    }

}
