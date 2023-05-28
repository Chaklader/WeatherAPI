package com.weather.api.weatherapi.dao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "geography", schema = "weather")
public class Geography extends AbstractEntity {

    private String country;
    private String city;
    private double latitude;
    private double longitude;

    @OneToOne
    private WeatherData weatherData;

}
