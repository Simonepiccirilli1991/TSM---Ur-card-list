package com.tsm.ur.card.frontend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${app.seor-api-url}")
    private String seorApiUrl;

    @Value("${app.auth-server-url}")
    private String authServerUrl;

    @Bean
    public WebClient seorWebClient() {
        return WebClient.builder()
                .baseUrl(seorApiUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public WebClient authWebClient() {
        return WebClient.builder()
                .baseUrl(authServerUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

