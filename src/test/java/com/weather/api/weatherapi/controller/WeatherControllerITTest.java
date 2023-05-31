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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



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

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GeographyRepository geographyRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeEach
    public void setUp() {
        geographyRepository.deleteAll();
        weatherRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        geographyRepository.deleteAll();
        weatherRepository.deleteAll();
    }

    @Test
    public void test_GetHistoricalWeatherByCoordinates() {

        WeatherData weatherData = DummyData.getWeatherData();
        Geography geography = DummyData.getGeography(weatherData);
        weatherData.setGeography(geography);

        weatherRepository.save(weatherData);
        geographyRepository.save(geography);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<WeatherDataDto[]> responseEntity = restTemplate.exchange(
            "http://localhost:" + port + "/mintos/v1/api/weather/coordinates?latitude=23.7&longitude=90.4",
            HttpMethod.GET,
            new HttpEntity<>(headers),
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
