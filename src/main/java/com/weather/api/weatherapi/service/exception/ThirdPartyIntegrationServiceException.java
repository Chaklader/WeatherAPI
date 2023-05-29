package com.weather.api.weatherapi.service.exception;

import com.weather.api.weatherapi.service.exception.base.BaseWeatherApiException;

public class ThirdPartyIntegrationServiceException extends BaseWeatherApiException {
    public ThirdPartyIntegrationServiceException(String userFriendlyMessage, String detailedTechnicalDescription) {
        super(userFriendlyMessage, detailedTechnicalDescription);
    }

    @Override
    public ExceptionErrorCode errorCode() {
        return ExceptionErrorCode.THIRD_PARTY_INTEGRATION_ERROR;
    }
}
