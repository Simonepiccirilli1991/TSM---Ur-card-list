package com.tsm.ur.card.frontend.service;

import com.tsm.ur.card.frontend.model.BaseRecap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecapService {

    private final WebClient seorWebClient;

    public List<BaseRecap> getRecap(String accessToken) {
        log.info("Recupero recap");
        try {
            return seorWebClient.get()
                    .uri("/api/v1/recap/getrecap")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<BaseRecap>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero recap: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}

