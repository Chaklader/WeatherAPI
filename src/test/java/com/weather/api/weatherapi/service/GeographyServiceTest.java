package com.weather.api.weatherapi.service;

import com.weather.api.weatherapi.controller.dto.GeographyDto;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import com.weather.api.weatherapi.dummy.DummyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GeographyServiceTest {


    @Mock
    private GeographyRepository geographyRepository;

    @InjectMocks
    private GeographyService geographyService;


    @Test
    public void test_getHistoricalQueriesByIp(){

        WeatherData weatherData = DummyData.getWeatherData();
        Geography geography = DummyData.getGeography(weatherData);

        List<Geography> geographyList = List.of(geography);

        Mockito.when(geographyRepository.findAllByIpAddress(anyString())).thenReturn(geographyList);

        List<GeographyDto> geographyDtosList = geographyService.getHistoricalQueriesByIp(anyString());

        assertEquals(1, geographyDtosList.size());
        GeographyDto geographyDto = geographyDtosList.get(0);

        assertEquals(geography.getCountry(), geographyDto.getCountry());
        assertEquals(geography.getCity(), geographyDto.getCity());
        assertEquals(geography.getLatitude(), geographyDto.getLatitude(), 0.001);
        assertEquals(geography.getLongitude(), geographyDto.getLongitude(), 0.001);
        assertEquals(geography.getIpAddress(), geographyDto.getIpAddress());

    }
}