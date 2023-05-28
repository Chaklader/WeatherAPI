package com.weather.api.weatherapi.service.client;

import com.weather.api.weatherapi.configuration.OkHttpClientConfig;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.concurrent.TimeUnit;


public enum OkHttpClientSingleton {
    INSTANCE;

    private OkHttpClient client;


    /*
        If the cache size (10 MB) is reached and the cache is full, the OkHttpClient will evict
        or overwrite the oldest entries in the cache to make room for new entries.
        By default, OkHttp uses a LRU (Least Recently Used) eviction policy for the cache. This
        means that when the cache reaches its maximum size, the least recently used entries will be
        removed first to accommodate new entries.
    * */
    public void initialize(ApplicationContext applicationContext) {
        OkHttpClientConfig config = applicationContext.getBean(OkHttpClientConfig.class);
        Cache cache = new Cache(new File(config.getCacheDirectory()), config.getCacheSize());
        client = new OkHttpClient.Builder()
            .addInterceptor(new DefaultContentTypeInterceptor("application/json"))
            .cache(cache)
            .eventListener(new WeatherLogEventsListener())
            .followRedirects(false)
            .readTimeout(1, TimeUnit.SECONDS)
            .build();
    }

    public OkHttpClient getClient() {
        return client;
    }
}

