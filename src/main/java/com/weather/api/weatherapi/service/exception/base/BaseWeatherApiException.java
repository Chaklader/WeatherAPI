package com.weather.api.weatherapi.service.exception.base;

import com.weather.api.weatherapi.service.exception.ExceptionErrorCode;

public abstract class BaseWeatherApiException extends RuntimeException {
    private final String detailedTechnicalDescription;
    private final String userFriendlyMessage;

    public BaseWeatherApiException(String userFriendlyMessage, String detailedTechnicalDescription) {
        this.detailedTechnicalDescription = detailedTechnicalDescription;
        this.userFriendlyMessage = userFriendlyMessage;
    }

    public abstract ExceptionErrorCode errorCode();

    public String getDetailedTechnicalDescription() {
        return detailedTechnicalDescription;
    }

    public String getUserFriendlyMessage() {
        return userFriendlyMessage;
    }
}
