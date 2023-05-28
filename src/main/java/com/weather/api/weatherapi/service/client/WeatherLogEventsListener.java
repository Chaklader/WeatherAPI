package com.weather.api.weatherapi.service.client;

import lombok.extern.log4j.Log4j2;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@Log4j2
public class WeatherLogEventsListener extends EventListener {

    private long start;

    @Override
    public void callStart(@NotNull Call call) {
        logTimedEvent("callStart");
    }

    @Override
    public void requestHeadersEnd(@NotNull Call call, Request request) {
        log.info("requestHeadersEnd at {} with headers {}", LocalDateTime.now(), request.headers());
    }

    @Override
    public void responseHeadersEnd(@NotNull Call call, Response response) {
        log.info("responseHeadersEnd at {} with headers {}", LocalDateTime.now(), response.headers());
    }

    @Override
    public void callEnd(@NotNull Call call) {
        log.info("callEnd at {}", LocalDateTime.now());
    }

    private void logTimedEvent(String name) {
        long now = System.nanoTime();
        if (name.equals("callStart")) {
            start = now;
        }
        long elapsedNanos = now - start;
        log.info(String.format("%.3f %s%n", elapsedNanos / 1000000000d, name));
    }
}