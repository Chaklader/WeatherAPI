package com.weather.api.weatherapi.utils;

public class Parameters {


    public static final String USER_FRIENDLY_MESSAGE = "Weather data is not available for the IP address";

    public static final String DETAILED_TECHNICAL_DESCRIPTION = "We can't retrieve the weather data from the Open Weather Map API";

    public static final String CONTENT_TYPE = "application/json";

    public static final String KEY_GENERATOR = "keyGenerator";

    public static final String CONTENT_TYPE_HEADER = "Content-Type";

    public static final String UNKNOWN = "unknown";
    public static final String WILDCARD_IP_ADDRESS = "0.0.0.0";

    public static final String[] IP_HEADER_CANDIDATES = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };


    public static final double MILES_PER_DEGREE = 69.0;
    public static final double SQUARE_SIZE = 10.0;
}
