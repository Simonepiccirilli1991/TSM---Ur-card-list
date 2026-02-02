package com.tsm.ur.card.wiam.exception;

import com.tsm.ur.card.wiam.exception.UtenteException;
import com.tsm.ur.card.wiam.model.ErrorWiamResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WiamExceptionHandler {


    @ExceptionHandler(UtenteException.class)
    public ResponseEntity<ErrorWiamResponse> utenteExceptionHandler(UtenteException ex) {
        var response = new ErrorWiamResponse(ex.getMessage(), ex.getErrore());
        var status = getStatus(ex.getCodice());
        return new ResponseEntity<>(response, status);
    }
}



    private HttpStatus getStatus(String codice){

        return switch (codice){
            case "WIAM-400x" -> HttpStatus.BAD_REQUEST;
            case "WIAM-404x" -> HttpStatus.NOT_FOUND;
            case "WIAM-500x" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }