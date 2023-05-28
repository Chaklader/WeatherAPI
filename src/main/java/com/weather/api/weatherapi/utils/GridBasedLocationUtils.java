package com.weather.api.weatherapi.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GridBasedLocationUtils {

    @Value("${weatherdata.grid.size}")
    private Double gridSize;

    public double roundToGrid(double coordinate) {
        return Math.round(coordinate / gridSize) * gridSize;
    }

}
