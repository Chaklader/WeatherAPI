package com.weather.api.weatherapi.service;


import com.weather.api.weatherapi.controller.dto.*;
import com.weather.api.weatherapi.controller.dto.mapper.GeographyMapper;
import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.repository.GeographyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;



@Log4j2
@RequiredArgsConstructor
@Service
public class GeographyService {

    private final GeographyRepository geographyRepository;

    public List<GeographyDto> getHistoricalQueriesByIp(String ipAddress) {
        List<Geography> allByIpAddress = geographyRepository.findAllByIpAddress(ipAddress);

        return allByIpAddress.stream().map(GeographyMapper::mapToDto).toList();
    }

}
