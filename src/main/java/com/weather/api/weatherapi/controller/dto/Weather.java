package com.weather.api.weatherapi.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Weather {
    private String icon;
    private String description;
    private String main;
    private int id;
}