package com.weather.api.weatherapi.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weather_data", schema = "weather")
public class WeatherData extends AbstractEntity{

    private double currentTemperature;
    private double minTemperature;
    private double maxTemperature;
    private double feelsLike;

    private int humidity;
    private int pressure;
    private int visibility;
    private double windSpeed;

    @Column(name = "query_timestamp")
    private Date queryTimestamp;

    @ToString.Exclude
    @OneToOne(mappedBy = "weatherData")
    private Geography geography;
}











