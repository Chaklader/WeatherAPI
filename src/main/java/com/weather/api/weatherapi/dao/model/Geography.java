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
@Table(name = "geography", schema = "weather")
public class Geography extends AbstractEntity {

    private String country;
    private String city;
    private double latitude;
    private double longitude;

    private String ipAddress;

    @Column(name = "query_timestamp")
    private Date queryTimestamp;

    @OneToOne
    private WeatherData weatherData;

}
