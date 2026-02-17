package com.tsm.ur.card.frontend.service;

import com.tsm.ur.card.frontend.model.BaseResponse;
import com.tsm.ur.card.frontend.model.form.VendiProdottoForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VenditaService {

    @Value("${seor.url}")
    private String seorUrl;

    private final WebClient.Builder webClientBuilder;

    public BaseResponse vendiProdotto(String accessToken, VendiProdottoForm form) {
        log.info("VenditaService - vendiProdotto per id: {}, tipo: {}", form.getIdProdotto(), form.getTipoProdotto());

        try {
            Map<String, Object> request = Map.of(
                    "idProdotto", form.getIdProdotto(),
                    "tipoProdotto", form.getTipoProdotto(),
                    "prezzoVendita", form.getPrezzoVendita(),
                    "costiVendita", form.getCostiVendita() != null ? form.getCostiVendita() : 0,
                    "dataVendita", form.getDataVendita(),
                    "piattaformaVendita", form.getPiattaformaVendita() != null ? form.getPiattaformaVendita() : "",
                    "note", form.getNote() != null ? form.getNote() : ""
            );

            return webClientBuilder.build()
                    .post()
                    .uri(seorUrl + "/api/v1/vendita/vendi")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .doOnSuccess(response -> log.info("vendiProdotto response: {}", response))
                    .doOnError(error -> log.error("vendiProdotto error: {}", error.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Errore durante la vendita: {}", e.getMessage());
            BaseResponse error = new BaseResponse();
            error.setMessage("Errore durante la vendita: " + e.getMessage());
            error.setSuccess(false);
            return error;
        }
    }
}

