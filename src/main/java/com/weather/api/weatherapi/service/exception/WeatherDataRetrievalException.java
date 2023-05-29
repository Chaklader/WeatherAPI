package com.weather.api.weatherapi.service.exception;

import com.weather.api.weatherapi.service.exception.base.BaseWeatherApiException;

public class WeatherDataRetrievalException extends BaseWeatherApiException {

    public WeatherDataRetrievalException(String userFriendlyMessage, String detailedTechnicalDescription) {
        super(userFriendlyMessage, detailedTechnicalDescription);
    }

    @Override
    public ExceptionErrorCode errorCode() {
        return ExceptionErrorCode.WEATHER_DATA_RETRIEVAL_ERROR;
    }
}
