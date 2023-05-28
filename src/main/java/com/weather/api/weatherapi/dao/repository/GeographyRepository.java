package com.weather.api.weatherapi.dao.repository;

import com.weather.api.weatherapi.dao.model.Geography;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GeographyRepository extends JpaRepository<Geography, String> {

}