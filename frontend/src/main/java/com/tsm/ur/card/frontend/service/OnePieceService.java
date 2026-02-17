package com.tsm.ur.card.frontend.service;

import com.tsm.ur.card.frontend.model.BaseResponse;
import com.tsm.ur.card.frontend.model.CartaOnePiece;
import com.tsm.ur.card.frontend.model.SealedOnePiece;
import com.tsm.ur.card.frontend.model.form.AggiungiCartaOnePieceForm;
import com.tsm.ur.card.frontend.model.form.AggiungiSealedOnePieceForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnePieceService {

    private final WebClient seorWebClient;

    // ===== CARDS =====

    public List<CartaOnePiece> getMyCards(String accessToken) {
        log.info("Recupero carte One Piece");
        try {
            return seorWebClient.get()
                    .uri("/api/v1/onepiece/get-cards-by-user")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CartaOnePiece>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero carte One Piece: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CartaOnePiece> getMyCardsByStato(String accessToken, String stato) {
        log.info("Recupero carte One Piece per stato: {}", stato);
        try {
            return seorWebClient.get()
                    .uri("/api/v1/onepiece/get-cards-bystato/{stato}", stato)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CartaOnePiece>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero carte per stato: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public BaseResponse addCard(String accessToken, AggiungiCartaOnePieceForm form) {
        log.info("Aggiunta carta One Piece: {}", form.getNome());
        try {
            return seorWebClient.post()
                    .uri("/api/v1/onepiece/add-card")
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

    public CartaOnePiece getCardById(String accessToken, String idCard) {
        log.info("Recupero carta One Piece: {}", idCard);
        try {
            return seorWebClient.get()
                    .uri("/api/v1/onepiece/get-card/{idCarta}", idCard)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(CartaOnePiece.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero carta: {}", e.getMessage());
            return null;
        }
    }

    public BaseResponse deleteCard(String accessToken, String idCard) {
        log.info("Eliminazione carta One Piece: {}", idCard);
        try {
            return seorWebClient.post()
                    .uri("/api/v1/onepiece/delete-card")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(Map.of("idCarta", idCard))
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

    // ===== SEALED =====

    public List<SealedOnePiece> getMySealed(String accessToken) {
        log.info("Recupero sealed One Piece");
        try {
            return seorWebClient.get()
                    .uri("/api/v1/onepiece/get-sealed-by-user")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<SealedOnePiece>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero sealed One Piece: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<SealedOnePiece> getMySealedByStato(String accessToken, String stato) {
        log.info("Recupero sealed One Piece per stato: {}", stato);
        try {
            return seorWebClient.get()
                    .uri("/api/v1/onepiece/get-sealed-bystato/{stato}", stato)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<SealedOnePiece>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero sealed per stato: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public SealedOnePiece getSealedById(String accessToken, String idSealed) {
        log.info("Recupero sealed One Piece: {}", idSealed);
        try {
            return seorWebClient.get()
                    .uri("/api/v1/onepiece/get-sealed/{idSealed}", idSealed)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(SealedOnePiece.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero sealed: {}", e.getMessage());
            return null;
        }
    }

    public BaseResponse addSealed(String accessToken, AggiungiSealedOnePieceForm form) {
        log.info("Aggiunta sealed One Piece: {}", form.getNome());
        try {
            return seorWebClient.post()
                    .uri("/api/v1/onepiece/add-sealed")
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
        log.info("Eliminazione sealed One Piece: {}", idSealed);
        try {
            return seorWebClient.post()
                    .uri("/api/v1/onepiece/delete-sealed")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(Map.of("idSealed", idSealed))
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

