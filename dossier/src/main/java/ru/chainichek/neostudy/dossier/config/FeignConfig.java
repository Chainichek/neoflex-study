package ru.chainichek.neostudy.dossier.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    public static final String AUTH_HEADER_NAME = "X-API-KEY";
    @Value("${app.client.deal.auth-token}")
    private String authToken;

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header(AUTH_HEADER_NAME, authToken);
    }
}
