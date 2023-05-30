package com.weather.api.weatherapi.controller;

import com.weather.api.weatherapi.controller.dto.Coordinate;
import com.weather.api.weatherapi.controller.dto.SimplifiedWeatherData;
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

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.equalTo;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

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


}