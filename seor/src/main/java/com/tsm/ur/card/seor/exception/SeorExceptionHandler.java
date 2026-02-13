package com.tsm.ur.card.seor.exception;

import com.tsm.ur.card.seor.model.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
@Slf4j
public class SeorExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<BaseResponse> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("Errore nella chiamata a WIAM: {} - {}", ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode())
                .body(new BaseResponse("Errore nella comunicazione con WIAM: " + ex.getMessage(), false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGenericException(Exception ex) {
        log.error("Errore generico: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse("Errore interno del server: " + ex.getMessage(), false));
    }
}

