package com.tsm.ur.card.frontend.service;

import com.tsm.ur.card.frontend.model.BaseResponse;
import com.tsm.ur.card.frontend.model.LoginRequest;
import com.tsm.ur.card.frontend.model.LoginResponse;
import com.tsm.ur.card.frontend.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final WebClient authWebClient;
    private final WebClient seorWebClient;

    public LoginResponse login(String username, String password) {
        log.info("Tentativo login per username: {}", username);
        try {
            LoginRequest request = new LoginRequest();
            request.setUsername(username);
            request.setPassword(password);

            log.debug("Invio richiesta login a auth server...");
            LoginResponse response = authWebClient.post()
                    .uri("/api/v1/auth/login")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(LoginResponse.class)
                    .block();

            log.info("Login response ricevuta: {}", response != null ? "Success" : "Null");
            if (response != null) {
                log.debug("Access token presente: {}", response.getAccessToken() != null);
            }
            return response;
        } catch (WebClientResponseException e) {
            log.error("Errore login - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setMessage("Credenziali non valide");
            return errorResponse;
        } catch (Exception e) {
            log.error("Errore generico durante login: ", e);
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setMessage("Errore di connessione al server");
            return errorResponse;
        }
    }

    public BaseResponse register(RegisterRequest request) {
        log.info("Tentativo registrazione per username: {}", request.getUsername());
        try {
            return seorWebClient.post()
                    .uri("/api/v1/utente/registra-utente")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore registrazione: {}", e.getMessage());
            BaseResponse errorResponse = new BaseResponse();
            errorResponse.setMessage("Errore durante la registrazione");
            errorResponse.setSuccess(false);
            return errorResponse;
        }
    }

    public BaseResponse recuperoPassword(String username, String nuovaPassword) {
        log.info("Tentativo recupero password per username: {}", username);
        try {
            var request = new java.util.HashMap<String, String>();
            request.put("username", username);
            request.put("nuovaPassword", nuovaPassword);

            return seorWebClient.post()
                    .uri("/api/v1/utente/recupero-password")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore recupero password: {}", e.getMessage());
            BaseResponse errorResponse = new BaseResponse();
            errorResponse.setMessage("Errore durante il recupero password");
            errorResponse.setSuccess(false);
            return errorResponse;
        }
    }
}

