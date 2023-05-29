package com.weather.api.weatherapi.service.exception;


public enum ExceptionErrorCode {
    ENTITY_NOT_FOUND("entity-not-found", Category.BUSINESS),
    THIRD_PARTY_INTEGRATION_ERROR("third-party-integration", Category.TECHNICAL),
    WEATHER_DATA_RETRIEVAL_ERROR("weather-data-retrieval-exception", Category.TECHNICAL);

    private final String shortName;
    private final Category category;

    ExceptionErrorCode(String shortName, Category category) {
        this.shortName = shortName;
        this.category = category;
    }

    public String getShortName() {
        return shortName;
    }

    public Category getCategory() {
        return category;
    }

    public enum Category {
        BUSINESS("business"),
        TECHNICAL("technical");

        private final String shortName;

        Category(String shortName) {
            this.shortName = shortName;
        }

        public String getShortName() {
            return shortName;
        }
    }
}
