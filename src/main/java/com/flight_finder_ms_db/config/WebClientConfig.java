package com.flight_finder_ms_db.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${serpapi.base.url}")
    private String serpApiBaseUrl;

    @Bean
    public WebClient serpApiWebClient() {
        return WebClient.builder()
                .baseUrl(serpApiBaseUrl)
                .build();
    }
}