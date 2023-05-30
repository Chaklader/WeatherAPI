package com.weather.api.weatherapi.service;

import static org.junit.jupiter.api.Assertions.*;

import com.weather.api.weatherapi.controller.dto.SimplifiedWeatherData;
import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import com.weather.api.weatherapi.dao.repository.WeatherRepository;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.springframework.test.util.ReflectionTestUtils.setField;


@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private OkHttpClient client;
    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private GeographyRepository geographyRepository;

    @InjectMocks
    private WeatherService weatherService;


    @Mock
    Call call;
    @Mock
    Response response;
    @Mock
    ResponseBody responseBody;


    @BeforeEach
    void setup() throws IOException {

        weatherService.setClient(client);

        setField(weatherService, "OPEN_WEATHER_MAP_API_KEY", "key");
        setField(weatherService, "OPEN_WEATHER_MAP_API_BASE_URL", "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s");

        when(client.newCall(any(Request.class))).thenReturn(call);
        when(response.isSuccessful()).thenReturn(true);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn(readFileAsString("src/test/resources/mock/mock_weather_data.json"));

        WeatherData mockWeatherData = new WeatherData();
        Geography mockGeography = new Geography();
        when(weatherRepository.save(any(WeatherData.class))).thenReturn(mockWeatherData);
        when(geographyRepository.save(any(Geography.class))).thenReturn(mockGeography);

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onResponse(call, response);
            return null;
        }).when(call).enqueue(any(Callback.class));
    }


    @Test
    void testGetWeatherDataByCoordinate() {
        Coordinate coordinate = Coordinate.builder().latitude(23.7).longitude(90.4).build();
        SimplifiedWeatherData simplifiedWeatherData = weatherService.getWeatherDataByCoordinate("103.150.26.242", coordinate);

        assertNotNull(simplifiedWeatherData, "The simplifiedWeatherData should not be null");
        assertEquals(simplifiedWeatherData.getVisibility(), 4000);
        assertEquals(simplifiedWeatherData.getTemperature(), 31.03);
        assertEquals(simplifiedWeatherData.getHumidity(), 51);
        assertEquals(simplifiedWeatherData.getPressure(), 1006);
        assertEquals(simplifiedWeatherData.getFeelsLike(), 32.87);
        assertEquals(simplifiedWeatherData.getWindSpeed(), 3.09);
    }


    private static String readFileAsString(String filePath) throws IOException {
        Path path = Path.of(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
}
