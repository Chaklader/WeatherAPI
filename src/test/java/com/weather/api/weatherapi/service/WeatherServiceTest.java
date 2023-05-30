package com.weather.api.weatherapi.service;

import static org.junit.jupiter.api.Assertions.*;

import com.weather.api.weatherapi.controller.dto.SimplifiedWeatherData;
import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.controller.dto.WeatherDataDto;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import com.weather.api.weatherapi.dao.repository.WeatherRepository;
import com.weather.api.weatherapi.dummy.DummyData;
import com.weather.api.weatherapi.service.exception.WeatherDataRetrievalException;
import com.weather.api.weatherapi.utils.FileUtils;
import com.weather.api.weatherapi.utils.Parameters;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.springframework.test.util.ReflectionTestUtils.setField;



@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    }


    @Test
    public void test_getWeatherDataByCoordinate_withApiSuccessful() throws IOException {

        doAnswerOnResponse();

        when(response.isSuccessful()).thenReturn(true);
        when(response.body()).thenReturn(responseBody);

        when(responseBody.string()).thenReturn(FileUtils.readFileAsString(MOCK_WEATHER_DATA_FILE));

        WeatherData weatherData = DummyData.getWeatherData();
        Geography geography = DummyData.getGeography(weatherData);
        weatherData.setGeography(geography);

        Coordinate coordinate = DummyData.getCoordinate();

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
    public void test_GetWeatherDataByCoordinate_WithApiFailure_AndDbData() {

        doAnswerOnResponse();

        when(response.isSuccessful()).thenReturn(false);

        WeatherData weatherData = DummyData.getWeatherData();
        Geography geography = DummyData.getGeography(weatherData);
        weatherData.setGeography(geography);

        when(geographyRepository.findFirstByIpAddressOrLatitudeAndLongitudeOrderByQueryTimestampDesc(anyString(), anyDouble(), anyDouble()))
            .thenReturn(Optional.of(geography));

        SimplifiedWeatherData simplifiedWeatherData = weatherService.getWeatherDataByCoordinate("103.150.26.242", DummyData.getCoordinate());

        assertNotNull(simplifiedWeatherData, "The simplifiedWeatherData should not be null");
        assertEquals(simplifiedWeatherData.getVisibility(), 4000);
        assertEquals(simplifiedWeatherData.getTemperature(), 31.03);
        assertEquals(simplifiedWeatherData.getHumidity(), 51);
        assertEquals(simplifiedWeatherData.getPressure(), 1006);
        assertEquals(simplifiedWeatherData.getFeelsLike(), 32.87);
        assertEquals(simplifiedWeatherData.getWindSpeed(), 3.09);
    }

    @Test
    public void test_GetWeatherDataByCoordinate_WithApiFailure_AndNoDbDataFailure() {

        doAnswerOnResponse();

        when(response.isSuccessful()).thenReturn(false);

        when(geographyRepository.findFirstByIpAddressOrLatitudeAndLongitudeOrderByQueryTimestampDesc(anyString(), anyDouble(), anyDouble()))
            .thenReturn(Optional.empty());

        Exception exception = assertThrows(WeatherDataRetrievalException.class, () -> {
            weatherService.getWeatherDataByCoordinate("103.150.26.242", DummyData.getCoordinate());
        });

        assertTrue(exception instanceof WeatherDataRetrievalException);
        assertEquals(((WeatherDataRetrievalException) exception).getUserFriendlyMessage(), Parameters.USER_FRIENDLY_MESSAGE);
        assertEquals(((WeatherDataRetrievalException) exception).getDetailedTechnicalDescription(), Parameters.DETAILED_TECHNICAL_DESCRIPTION);
    }


    @Test
    public void test_GetWeatherDataByCoordinate_OkHttpClientOnFailure_WithApiFailure_AndNoDbDataFailure() {

        onAnswerOnFailure();

        when(geographyRepository.findFirstByIpAddressOrLatitudeAndLongitudeOrderByQueryTimestampDesc(anyString(), anyDouble(), anyDouble()))
            .thenReturn(Optional.empty());

        Exception exception = assertThrows(WeatherDataRetrievalException.class, () -> {
            weatherService.getWeatherDataByCoordinate("103.150.26.242", DummyData.getCoordinate());
        });

        assertTrue(exception instanceof WeatherDataRetrievalException);
        assertEquals(((WeatherDataRetrievalException) exception).getUserFriendlyMessage(), Parameters.USER_FRIENDLY_MESSAGE);
        assertEquals(((WeatherDataRetrievalException) exception).getDetailedTechnicalDescription(), Parameters.DETAILED_TECHNICAL_DESCRIPTION);
    }


    @Test
    public void test_GetWeatherDataByCoordinate_OkHttpClientOnFailure_WithApiFailure_AndNoDbDataSuccessful() {

        onAnswerOnFailure();

        WeatherData weatherData = DummyData.getWeatherData();
        Geography geography = DummyData.getGeography(weatherData);
        weatherData.setGeography(geography);

        when(geographyRepository.findFirstByIpAddressOrLatitudeAndLongitudeOrderByQueryTimestampDesc(anyString(), anyDouble(), anyDouble()))
            .thenReturn(Optional.of(geography));

        SimplifiedWeatherData simplifiedWeatherData = weatherService.getWeatherDataByCoordinate("103.150.26.242", DummyData.getCoordinate());

        assertNotNull(simplifiedWeatherData, "The simplifiedWeatherData should not be null");
        assertEquals(simplifiedWeatherData.getVisibility(), 4000);
        assertEquals(simplifiedWeatherData.getTemperature(), 31.03);
        assertEquals(simplifiedWeatherData.getHumidity(), 51);
        assertEquals(simplifiedWeatherData.getPressure(), 1006);
        assertEquals(simplifiedWeatherData.getFeelsLike(), 32.87);
        assertEquals(simplifiedWeatherData.getWindSpeed(), 3.09);
    }

    @Test
    public void test_getHistoricalWeatherByCoordinates(){

        WeatherData weatherData = DummyData.getWeatherData();
        Geography geography = DummyData.getGeography(weatherData);
        weatherData.setGeography(geography);

        List<WeatherData> weatherDataList = List.of(weatherData);

        when(weatherRepository.findAllByGeography_LatitudeAndGeography_Longitude(anyDouble(), anyDouble()))
            .thenReturn(weatherDataList);

        List<WeatherDataDto> historicalWeatherData = weatherService.getHistoricalWeatherByCoordinates(23.7, 90.4);

        assertNotNull(historicalWeatherData);
        assertEquals(1, historicalWeatherData.size());

        WeatherDataDto weatherDataDto = historicalWeatherData.get(0);

        assertEquals(weatherData.getCurrentTemperature(), weatherDataDto.getCurrentTemperature());
        assertEquals(weatherData.getMinTemperature(), weatherDataDto.getMinTemperature());
        assertEquals(weatherData.getMaxTemperature(), weatherDataDto.getMaxTemperature());
        assertEquals(weatherData.getFeelsLike(), weatherDataDto.getFeelsLike());
        assertEquals(weatherData.getHumidity(), weatherDataDto.getHumidity());
        assertEquals(weatherData.getPressure(), weatherDataDto.getPressure());
        assertEquals(weatherData.getVisibility(), weatherDataDto.getVisibility());
        assertEquals(weatherData.getWindSpeed(), weatherDataDto.getWindSpeed());
        assertEquals(weatherData.getQueryTimestamp(), weatherDataDto.getQueryTimestamp());
        assertEquals(geography.getIpAddress(), weatherDataDto.getIpAddress());

    }

    private void doAnswerOnResponse() {
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onResponse(call, response);
            return null;
        }).when(call).enqueue(any(Callback.class));
    }

    private void onAnswerOnFailure() {
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onFailure(call, new IOException());
            return null;
        }).when(call).enqueue(any(Callback.class));
    }
}
