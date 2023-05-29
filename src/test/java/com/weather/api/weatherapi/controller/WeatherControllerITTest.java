package com.weather.api.weatherapi.controller;

import com.weather.api.weatherapi.service.WeatherService;
import jakarta.validation.constraints.NotNull;
import okhttp3.*;
import okio.Timeout;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.equalTo;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerITTest {

    @Value("${openweathermap.api.key}")
    private String OPEN_WEATHER_MAP_API_KEY;

    @Value("${openweathermap.base.url}")
    private String OPEN_WEATHER_MAP_API_BASE_URL;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OkHttpClient httpClient;

//    @InjectMocks
//    private WeatherService weatherService;



    @Test
    public void testGetWeather() throws Exception {

        String OPEN_WEATHER_MAP_QUERY_URL = String.format(OPEN_WEATHER_MAP_API_BASE_URL, 23.7, 90.4, OPEN_WEATHER_MAP_API_KEY);

        String mockResponseJson = readJsonFileAsString("src/main/resources/mock/mock_simplified_weather_data.json");

        ResponseBody mockResponseBody = ResponseBody.create(MediaType.parse("application/json"), mockResponseJson);
        Response mockResponse = new Response.Builder()
            .request(new Request.Builder().url(OPEN_WEATHER_MAP_QUERY_URL).build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(mockResponseBody)
            .build();

        Mockito.when(httpClient.newCall(any(Request.class))).thenReturn(new Call() {
            @org.jetbrains.annotations.NotNull
            @Override
            public Timeout timeout() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }

            @Override
            public Response execute() throws IOException {
                return mockResponse;
            }

            @Override
            public void enqueue(Callback responseCallback) {
                try {
                    responseCallback.onResponse(this, mockResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void cancel() {
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @NotNull
            @Override
            public Call clone() {
                return null;
            }
        });

        mockMvc.perform(get("/v1/api/weather").header("X-Forwarded-For", "103.150.26.242"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.visibility", equalTo(4000)))
            .andExpect(jsonPath("$.temperature", equalTo(31.03)))
            .andExpect(jsonPath("$.humidity", equalTo(55)))
            .andExpect(jsonPath("$.pressure", equalTo(1007)))
            .andExpect(jsonPath("$.feelsLike", equalTo(33.72)))
            .andExpect(jsonPath("$.windSpeed", equalTo(3.09)));

    }

    public String readJsonFileAsString(String s) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of(s));
        return new String(bytes);
    }


}