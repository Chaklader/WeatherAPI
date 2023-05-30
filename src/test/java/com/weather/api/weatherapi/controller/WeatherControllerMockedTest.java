package com.weather.api.weatherapi.controller;

import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.controller.dto.SimplifiedWeatherData;
import com.weather.api.weatherapi.controller.dto.WeatherDataDto;
import com.weather.api.weatherapi.service.LocationService;
import com.weather.api.weatherapi.service.WeatherService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.equalTo;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerMockedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private LocationService locationService;


    @Test
    public void test_getWeather() throws Exception {

        Coordinate mockCoordinate = Coordinate.builder().latitude(23.7).longitude(90.5).build();
        Pair<String, Coordinate> mockLocation = Pair.of("103.150.26.242", mockCoordinate);
        Mockito.when(locationService.retrieveCoordinateFromIpAddress()).thenReturn(mockLocation);

        SimplifiedWeatherData mockData = new SimplifiedWeatherData(4000, 31.03, 55, 1007, 33.72, 3.09);
        Mockito.when(weatherService.getWeatherDataByCoordinate(anyString(), any(Coordinate.class))).thenReturn(mockData);

        mockMvc.perform(get("/v1/api/weather").header("X-Forwarded-For", "103.150.26.242"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.visibility", equalTo(4000)))
            .andExpect(jsonPath("$.temperature", equalTo(31.03)))
            .andExpect(jsonPath("$.humidity", equalTo(55)))
            .andExpect(jsonPath("$.pressure", equalTo(1007)))
            .andExpect(jsonPath("$.feelsLike", equalTo(33.72)))
            .andExpect(jsonPath("$.windSpeed", equalTo(3.09)));

    }


    @Test
    public void test_getHistoricalWeatherByCoordinates() throws Exception {

        WeatherDataDto weatherDataDto = WeatherDataDto.builder()
            .currentTemperature(304.18)
            .minTemperature(304.18)
            .maxTemperature(304.18)
            .feelsLike(306.02)
            .humidity(51)
            .pressure(1006)
            .visibility(4000)
            .windSpeed(3.09)
            .queryTimestamp(new Date())
            .ipAddress("103.150.26.242")
            .build();

        List<WeatherDataDto> weatherDataDtosList = List.of(weatherDataDto);


        Mockito.when(weatherService.getHistoricalWeatherByCoordinates(anyDouble(), anyDouble())).thenReturn(weatherDataDtosList);

        mockMvc.perform(get("http://localhost:" + 8080 + "/v1/api/weather/coordinates?latitude=23.7&longitude=90.4")
                .header("X-Forwarded-For", "103.150.26.242"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].currentTemperature").value(304.18))
            .andExpect(jsonPath("$[0].minTemperature").value(304.18))
            .andExpect(jsonPath("$[0].maxTemperature").value(304.18))
            .andExpect(jsonPath("$[0].feelsLike").value(306.02))
            .andExpect(jsonPath("$[0].humidity").value(51))
            .andExpect(jsonPath("$[0].pressure").value(1006))
            .andExpect(jsonPath("$[0].visibility").value(4000))
            .andExpect(jsonPath("$[0].windSpeed").value(3.09))
            .andExpect(jsonPath("$[0].queryTimestamp").exists())
            .andExpect(jsonPath("$[0].ipAddress").value("103.150.26.242"));
    }


}