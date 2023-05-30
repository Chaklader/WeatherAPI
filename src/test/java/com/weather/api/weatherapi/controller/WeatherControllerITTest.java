package com.weather.api.weatherapi.controller;

import com.weather.api.weatherapi.controller.dto.WeatherDataDto;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import com.weather.api.weatherapi.dao.repository.WeatherRepository;
import com.weather.api.weatherapi.dummy.DummyData;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class WeatherControllerITTest {

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("weatherdb")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private GeographyRepository geographyRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeEach
    public void setUp() {
        restTemplate.getRestTemplate().setInterceptors(Collections.singletonList(new BasicAuthenticationInterceptor("test", "test")));
    }

    @AfterEach
    public void tearDown() {
        geographyRepository.deleteAll();
        weatherRepository.deleteAll();
    }

    @Test
    public void testGetHistoricalWeatherByCoordinates() {

        WeatherData weatherData = DummyData.getWeatherData();
        Geography geography = DummyData.getGeography(weatherData);
        weatherData.setGeography(geography);

        weatherRepository.save(weatherData);
        geographyRepository.save(geography);

        ResponseEntity<WeatherDataDto[]> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/v1/api/weather/coordinates?latitude=23.7&longitude=90.4",
                WeatherDataDto[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        WeatherDataDto[] weatherDataDtos = responseEntity.getBody();
        assertNotNull(weatherDataDtos);
        assertEquals(1, weatherDataDtos.length);

        WeatherDataDto weatherDataDto = weatherDataDtos[0];
        assertEquals(weatherData.getCurrentTemperature(), weatherDataDto.getCurrentTemperature());
        assertEquals(weatherData.getMinTemperature(), weatherDataDto.getMinTemperature());
        assertEquals(weatherData.getMaxTemperature(), weatherDataDto.getMaxTemperature());
        assertEquals(weatherData.getFeelsLike(), weatherDataDto.getFeelsLike());
        assertEquals(weatherData.getHumidity(), weatherDataDto.getHumidity());
        assertEquals(weatherData.getPressure(), weatherDataDto.getPressure());
        assertEquals(weatherData.getVisibility(), weatherDataDto.getVisibility());
        assertEquals(weatherData.getWindSpeed(), weatherDataDto.getWindSpeed());
    }

}