package com.weather.api.weatherapi;

import com.weather.api.weatherapi.controller.WeatherControllerITTest;
import com.weather.api.weatherapi.controller.WeatherControllerMockedTest;
import com.weather.api.weatherapi.service.GeographyServiceTest;
import com.weather.api.weatherapi.service.LocationServiceTest;
import com.weather.api.weatherapi.service.WeatherServiceTest;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.*;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
@SuiteDisplayName("My Test Suite")
@SelectClasses({
    WeatherControllerMockedTest.class,
    WeatherControllerITTest.class,
    GeographyServiceTest.class,
    LocationServiceTest.class,
    WeatherServiceTest.class
})
@Suite
public class WeatherApiTestSuite {

}

