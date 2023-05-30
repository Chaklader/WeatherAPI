package com.weather.api.weatherapi.service;

import static org.junit.jupiter.api.Assertions.*;

import com.weather.api.weatherapi.controller.dto.SimplifiedWeatherData;
import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import com.weather.api.weatherapi.dao.repository.WeatherRepository;
import com.weather.api.weatherapi.service.exception.WeatherDataRetrievalException;
import com.weather.api.weatherapi.utils.Parameters;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.springframework.test.util.ReflectionTestUtils.setField;



@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    public static final String MOCK_WEATHER_DATA_FILE = "src/test/resources/mock/mock_weather_data.json";


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
    void setup() {

        weatherService.setClient(client);

        setField(weatherService, "OPEN_WEATHER_MAP_API_KEY", "key");
        setField(weatherService, "OPEN_WEATHER_MAP_API_BASE_URL", "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s");

        when(client.newCall(any(Request.class))).thenReturn(call);
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onResponse(call, response);
            return null;
        }).when(call).enqueue(any(Callback.class));
    }


    @Test
    void test_getWeatherDataByCoordinate_withApiSuccessful() throws IOException {

        when(response.isSuccessful()).thenReturn(true);
        when(response.body()).thenReturn(responseBody);

        when(responseBody.string()).thenReturn(readFileAsString(MOCK_WEATHER_DATA_FILE));

        WeatherData weatherData = getWeatherData();
        Geography geography = getGeography(weatherData);
        weatherData.setGeography(geography);

        Coordinate coordinate = getCoordinate();

        when(weatherRepository.save(any(WeatherData.class))).thenReturn(weatherData);
        when(geographyRepository.save(any(Geography.class))).thenReturn(geography);


        SimplifiedWeatherData simplifiedWeatherData = weatherService.getWeatherDataByCoordinate("103.150.26.242", coordinate);

        assertNotNull(simplifiedWeatherData, "The simplifiedWeatherData should not be null");
        assertEquals(simplifiedWeatherData.getVisibility(), 4000);
        assertEquals(simplifiedWeatherData.getTemperature(), 31.03);
        assertEquals(simplifiedWeatherData.getHumidity(), 51);
        assertEquals(simplifiedWeatherData.getPressure(), 1006);
        assertEquals(simplifiedWeatherData.getFeelsLike(), 32.87);
        assertEquals(simplifiedWeatherData.getWindSpeed(), 3.09);
    }


    @Test
    void test_GetWeatherDataByCoordinate_WithApiFailure_AndDbData() {

        when(response.isSuccessful()).thenReturn(false);

        WeatherData weatherData = getWeatherData();
        Geography geography = getGeography(weatherData);
        weatherData.setGeography(geography);

        Coordinate coordinate = getCoordinate();

        when(geographyRepository.findFirstByIpAddressOrLatitudeAndLongitudeOrderByQueryTimestampDesc(anyString(), anyDouble(), anyDouble()))
            .thenReturn(Optional.of(geography));

        SimplifiedWeatherData simplifiedWeatherData = weatherService.getWeatherDataByCoordinate("103.150.26.242", coordinate);

        assertNotNull(simplifiedWeatherData, "The simplifiedWeatherData should not be null");
        assertEquals(simplifiedWeatherData.getVisibility(), 4000);
        assertEquals(simplifiedWeatherData.getTemperature(), 31.03);
        assertEquals(simplifiedWeatherData.getHumidity(), 51);
        assertEquals(simplifiedWeatherData.getPressure(), 1006);
        assertEquals(simplifiedWeatherData.getFeelsLike(), 32.87);
        assertEquals(simplifiedWeatherData.getWindSpeed(), 3.09);
    }



    @Test
    void test_GetWeatherDataByCoordinate_WithApiFailure_AndNoDbDataFailure() {
        when(response.isSuccessful()).thenReturn(false);

        when(geographyRepository.findFirstByIpAddressOrLatitudeAndLongitudeOrderByQueryTimestampDesc(anyString(), anyDouble(), anyDouble()))
            .thenReturn(Optional.empty());

        Exception exception = assertThrows(WeatherDataRetrievalException.class, () -> {
            weatherService.getWeatherDataByCoordinate("103.150.26.242", getCoordinate());
        });

        assertTrue(exception instanceof WeatherDataRetrievalException);
        assertEquals(((WeatherDataRetrievalException) exception).getUserFriendlyMessage(), Parameters.USER_FRIENDLY_MESSAGE);
        assertEquals(((WeatherDataRetrievalException) exception).getDetailedTechnicalDescription(), Parameters.DETAILED_TECHNICAL_DESCRIPTION);
    }






    public Geography getGeography(WeatherData weatherData) {
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

    public WeatherData getWeatherData() {
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

    public Coordinate getCoordinate() {
        return Coordinate.builder().latitude(23.7).longitude(90.4).build();
    }

    public static String readFileAsString(String filePath) throws IOException {
        Path path = Path.of(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
}
