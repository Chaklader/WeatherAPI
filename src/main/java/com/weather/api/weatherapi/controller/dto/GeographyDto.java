package com.weather.api.weatherapi.controller.dto;

import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeographyDto {

    private String country;
    private String city;
    private double latitude;
    private double longitude;

    private String ipAddress;
    private Date queryTimestamp;

}