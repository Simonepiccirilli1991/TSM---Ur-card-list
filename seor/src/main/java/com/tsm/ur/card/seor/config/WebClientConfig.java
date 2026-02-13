package com.tsm.ur.card.seor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${wiam.base-url}")
    private String wiamBaseUrl;

    @Value("${wiam.timeout:30000}")
    private int timeout;

    @Bean
    public WebClient wiamWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(wiamBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}

