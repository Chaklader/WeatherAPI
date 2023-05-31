package com.weather.api.weatherapi.config;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

import java.util.Collections;

@Configuration
public class RestTemplateConfiguration {

    public TestRestTemplate createTestRestTemplate(){

        TestRestTemplate restTemplate = new TestRestTemplate();

        restTemplate.getRestTemplate().setInterceptors(Collections.singletonList(new BasicAuthenticationInterceptor("test", "test")));
        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request,body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });
        return restTemplate;
    }
}
