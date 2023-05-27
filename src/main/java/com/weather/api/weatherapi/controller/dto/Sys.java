package com.weather.api.weatherapi.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sys {
    private String country;
    private long sunrise;
    private long sunset;
    private int id;
    private int type;
}