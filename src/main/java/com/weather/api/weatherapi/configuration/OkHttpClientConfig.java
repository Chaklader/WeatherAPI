package com.weather.api.weatherapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OkHttpClientConfig {

    @Value("#{${cache.size}}")
    private int cacheSize;

    @Value("${cache.directory}")
    private String cacheDirectory;

    public int getCacheSize() {
        return cacheSize;
    }

    public String getCacheDirectory() {
        return cacheDirectory;
    }
}
