package com.weather.api.weatherapi.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Clouds {
    private int all;
}