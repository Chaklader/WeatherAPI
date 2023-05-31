package com.weather.api.weatherapi.controller;


import com.weather.api.weatherapi.controller.dto.GeographyDto;
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
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
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
public class HistoricalAnalysisControllerITTest {

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
        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });
    }

    @AfterEach
    public void tearDown() {
        geographyRepository.deleteAll();
        weatherRepository.deleteAll();
    }

    @Test
    public void test_getHistoricalQueriesByIp() {

        WeatherData weatherData = DummyData.getWeatherData();
        Geography geography = DummyData.getGeography(weatherData);
        weatherData.setGeography(geography);

        weatherRepository.save(weatherData);
        geographyRepository.save(geography);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);


        String URL = "http://localhost:" + port + "/mintos/v1/api/queries/ip/103.150.26.242";

        ResponseEntity<GeographyDto[]> responseEntity = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            GeographyDto[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        GeographyDto[] geographyDtos = responseEntity.getBody();
        assertNotNull(geographyDtos);
        assertEquals(1, geographyDtos.length);

        GeographyDto geographyDto = geographyDtos[0];

        assertEquals(geography.getCountry(), geographyDto.getCountry());
        assertEquals(geography.getCity(), geographyDto.getCity());
        assertEquals(geography.getLatitude(), geographyDto.getLatitude());
        assertEquals(geography.getLongitude(), geographyDto.getLongitude());
        assertEquals(geography.getIpAddress(), geographyDto.getIpAddress());

    }

}
