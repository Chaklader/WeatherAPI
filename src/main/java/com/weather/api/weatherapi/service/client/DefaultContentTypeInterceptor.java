package com.weather.api.weatherapi.service.client;

import com.weather.api.weatherapi.utils.Parameters;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DefaultContentTypeInterceptor implements Interceptor {
    private final String contentType;

    public DefaultContentTypeInterceptor(String contentType) {
        this.contentType = contentType;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request requestWithContentType = originalRequest.newBuilder()
            .header(Parameters.CONTENT_TYPE_HEADER, contentType)
            .build();

        return chain.proceed(requestWithContentType);
    }
}
