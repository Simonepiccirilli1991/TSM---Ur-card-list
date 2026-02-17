package com.tsm.ur.card.frontend.service;

import com.tsm.ur.card.frontend.model.BaseResponse;
import com.tsm.ur.card.frontend.model.CartaPokemon;
import com.tsm.ur.card.frontend.model.SealedPokemon;
import com.tsm.ur.card.frontend.model.form.AggiungiCartaPokemonForm;
import com.tsm.ur.card.frontend.model.form.AggiungiSealedPokemonForm;
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
public class PokemonService {

    private final WebClient seorWebClient;

    // ===================== CARTE POKEMON =====================

    public List<CartaPokemon> getMyCards(String accessToken) {
        log.info("Recupero carte Pokemon");
        try {
            return seorWebClient.get()
                    .uri("/api/v1/pokemon/card/getcardsbyusername")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CartaPokemon>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero carte: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CartaPokemon> getMyCardsByStato(String accessToken, String stato) {
        log.info("Recupero carte Pokemon per stato: {}", stato);
        try {
            return seorWebClient.get()
                    .uri("/api/v1/pokemon/card/getcardbyUsernameandstato/{stato}", stato)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CartaPokemon>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero carte per stato: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public CartaPokemon getCardById(String accessToken, String idCarta) {
        log.info("Recupero carta Pokemon: {}", idCarta);
        try {
            return seorWebClient.get()
                    .uri("/api/v1/pokemon/card/getcard/{idCarta}", idCarta)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(CartaPokemon.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero carta: {}", e.getMessage());
            return null;
        }
    }

    public BaseResponse addCard(String accessToken, AggiungiCartaPokemonForm form) {
        log.info("Aggiunta carta Pokemon: {}", form.getNome());
        try {
            return seorWebClient.post()
                    .uri("/api/v1/pokemon/card/addcard")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(form)
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore aggiunta carta: {}", e.getMessage());
            BaseResponse error = new BaseResponse();
            error.setSuccess(false);
            error.setMessage("Errore durante l'aggiunta della carta");
            return error;
        }
    }

    public BaseResponse deleteCard(String accessToken, String idCarta) {
        log.info("Eliminazione carta Pokemon: {}", idCarta);
        try {
            return seorWebClient.method(org.springframework.http.HttpMethod.DELETE)
                    .uri("/api/v1/pokemon/card/cancellacarta")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(java.util.Map.of("idCarta", idCarta))
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore eliminazione carta: {}", e.getMessage());
            BaseResponse error = new BaseResponse();
            error.setSuccess(false);
            error.setMessage("Errore durante l'eliminazione");
            return error;
        }
    }

    // ===================== SEALED POKEMON =====================

    public List<SealedPokemon> getMySealed(String accessToken) {
        log.info("Recupero sealed Pokemon");
        try {
            return seorWebClient.get()
                    .uri("/api/v1/pokemon/card/getSealedByUsername")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<SealedPokemon>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero sealed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<SealedPokemon> getMySealedByStato(String accessToken, String stato) {
        log.info("Recupero sealed Pokemon per stato: {}", stato);
        try {
            return seorWebClient.get()
                    .uri("/api/v1/pokemon/card/getSealedByUsernameAndStato/{stato}", stato)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<SealedPokemon>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero sealed per stato: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public SealedPokemon getSealedById(String accessToken, String idSealed) {
        log.info("Recupero sealed Pokemon: {}", idSealed);
        try {
            return seorWebClient.get()
                    .uri("/api/v1/pokemon/card/getsealedbyid/{idSealed}", idSealed)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(SealedPokemon.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero sealed: {}", e.getMessage());
            return null;
        }
    }

    public BaseResponse addSealed(String accessToken, AggiungiSealedPokemonForm form) {
        log.info("Aggiunta sealed Pokemon: {}", form.getNome());
        try {
            return seorWebClient.post()
                    .uri("/api/v1/pokemon/card/addsealed")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(form)
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore aggiunta sealed: {}", e.getMessage());
            BaseResponse error = new BaseResponse();
            error.setSuccess(false);
            error.setMessage("Errore durante l'aggiunta del sealed");
            return error;
        }
    }

    public BaseResponse deleteSealed(String accessToken, String idSealed) {
        log.info("Eliminazione sealed Pokemon: {}", idSealed);
        try {
            return seorWebClient.method(org.springframework.http.HttpMethod.DELETE)
                    .uri("/api/v1/pokemon/card/cancellasealed")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(java.util.Map.of("idSealed", idSealed))
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore eliminazione sealed: {}", e.getMessage());
            BaseResponse error = new BaseResponse();
            error.setSuccess(false);
            error.setMessage("Errore durante l'eliminazione");
            return error;
        }
    }
}

