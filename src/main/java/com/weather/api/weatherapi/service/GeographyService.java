package com.weather.api.weatherapi.service;


import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import com.weather.api.weatherapi.dao.repository.WeatherRepository;
import com.weather.api.weatherapi.utils.GeographicalWeatherDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Log4j2
@RequiredArgsConstructor
@Service
public class GeographyService {

    private final GeographyRepository geographyRepository;

    public List<GeographyDto> getHistoricalQueriesByIp(String ipAddress) {
        final List<Geography> allByIpAddress = geographyRepository.findAllByIpAddress(ipAddress);

        return allByIpAddress.stream().map(GeographyMapper::mapToDto).toList();
    }

}
