package com.weather.api.weatherapi.utils;

public class MathUtils {

    public static Double getDoubleWithTwoDecimalPoints(double value){
        return Math.round(value * 100.0) / 100.0;
    }
}
