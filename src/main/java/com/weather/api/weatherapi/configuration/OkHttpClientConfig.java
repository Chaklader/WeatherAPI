package com.weather.api.weatherapi.configuration;

import com.weather.api.weatherapi.configuration.client.DefaultContentTypeInterceptor;
import com.weather.api.weatherapi.configuration.client.WeatherLogEventsListener;
import com.weather.api.weatherapi.utils.Parameters;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

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


    @Bean
    public OkHttpClient okHttpClient() {
        Cache cache = new Cache(new File(getCacheDirectory()), getCacheSize());

        return new OkHttpClient.Builder()
            .addInterceptor(new DefaultContentTypeInterceptor(Parameters.CONTENT_TYPE))
            .cache(cache)
            .eventListener(new WeatherLogEventsListener())
            .followRedirects(false)
            .readTimeout(1, TimeUnit.SECONDS)
            .build();
    }

}
