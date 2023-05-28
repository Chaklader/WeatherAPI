package com.weather.api.weatherapi.dao.repository;

import com.weather.api.weatherapi.dao.model.Geography;
import com.weather.api.weatherapi.dao.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface GeographyRepository extends JpaRepository<Geography, String> {

    Optional<Geography> findFirstByIpAddressOrLatitudeAndLongitudeOrderByQueryTimestampDesc(String ipAddress, double latitude, double longitude);

    List<Geography> findAllByIpAddress(String ipAddress);
}