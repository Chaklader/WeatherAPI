package com.weather.api.weatherapi.service.exception;

import com.weather.api.weatherapi.service.exception.base.BaseWeatherApiException;

public class EntityNotFoundException extends BaseWeatherApiException {

    public EntityNotFoundException(String userFriendlyMessage, String detailedTechnicalDescription) {
        super(userFriendlyMessage, detailedTechnicalDescription);
    }


    @Override
    public ExceptionErrorCode errorCode() {
        return ExceptionErrorCode.ENTITY_NOT_FOUND;
    }
}
