package com.weather.api.weatherapi.utils;

import com.weather.api.weatherapi.controller.dto.Coordinate;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.weather.api.weatherapi.utils.Parameters.MILES_PER_DEGREE;
import static com.weather.api.weatherapi.utils.Parameters.SQUARE_SIZE;

/*
Earth is not a perfect sphere, but an oblate spheroid. This means that calculations involving latitude and longitude can
become quite complex and might not result in perfect squares due to the Earth's curvature.

For a simplified representation, we can ignore Earth's curvature and treat it as a planar surface, considering that 1 degree
of latitude is approximately 69 miles, and one degree of longitude is approximately 69 miles at the equator but decreases as
we move towards the poles.

* */

@Order(1)
@Component
@Slf4j
public class GeoSquare {


    @Autowired
    private TaskExecutor taskExecutor;


    @Async("taskExecutor")
    @PostConstruct
    public void init() {
        taskExecutor.execute(() -> {
            final List<Coordinate> coordinates = calculateSquareCenters();
            log.info("THE NUMBER OF SQUARES ARE : " + coordinates.size());
        });
    }



    public static List<Coordinate> calculateSquareCenters() {
        List<Double> latitudes = IntStream.rangeClosed(-90, 90)
            .mapToDouble(i -> i * (SQUARE_SIZE / MILES_PER_DEGREE))
            .boxed().toList();

        List<Double> longitudes = IntStream.rangeClosed(-180, 180)
            .mapToDouble(i -> i * (SQUARE_SIZE / (MILES_PER_DEGREE)))
            .boxed().toList();

        return latitudes.stream()
                .flatMap(lat -> longitudes.stream()
                        .map(lon -> Coordinate.builder()
                                .latitude(lat + SQUARE_SIZE / (2 * MILES_PER_DEGREE))
                                .longitude(lon + SQUARE_SIZE / (2 * MILES_PER_DEGREE * Math.cos(Math.toRadians(lat))))
                                .build()))
                .collect(Collectors.toList());
    }

    public static Coordinate normalizeLocation(Coordinate coordinate) {
        double normalizedLat = Math.round(coordinate.getLatitude() / (SQUARE_SIZE / MILES_PER_DEGREE)) * (SQUARE_SIZE / MILES_PER_DEGREE);
        double normalizedLon = Math.round(coordinate.getLongitude() / (SQUARE_SIZE / (MILES_PER_DEGREE * Math.cos(Math.toRadians(coordinate.getLatitude()))))) * (SQUARE_SIZE / (MILES_PER_DEGREE * Math.cos(Math.toRadians(coordinate.getLatitude()))));

        return Coordinate.builder()
                .latitude(normalizedLat + SQUARE_SIZE / (2 * MILES_PER_DEGREE))
                .longitude(normalizedLon + SQUARE_SIZE / (2 * MILES_PER_DEGREE * Math.cos(Math.toRadians(normalizedLat))))
                .build();
    }

}
