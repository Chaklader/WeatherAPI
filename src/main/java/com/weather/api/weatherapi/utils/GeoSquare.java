package com.weather.api.weatherapi.utils;

import com.weather.api.weatherapi.controller.dto.Coordinate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
Creating a program to map the Earth into 10 mile by 10 mile squares involves dealing with complex geographic calculations. Keep in mind that Earth is not a perfect sphere, but an oblate spheroid. This means that calculations involving latitude and longitude can become quite complex and might not result in perfect squares due to the Earth's curvature.

However, for a simplified representation, we can ignore Earth's curvature and treat it as a planar surface, considering that one degree of latitude is approximately 69 miles, and one degree of longitude is approximately 69 miles at the equator but decreases as we move towards the poles.

* */
public class GeoSquare {


    public static final double MILES_PER_DEGREE = 69.0;
    public static final double SQUARE_SIZE = 10.0;

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
