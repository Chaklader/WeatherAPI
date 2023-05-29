package com.weather.api.weatherapi.dao.repository;

import com.weather.api.weatherapi.dao.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, String> {

    List<WeatherData> findAllByGeography_LatitudeAndGeography_Longitude(double latitude, double longitude);
}