package com.tsm.ur.card.wiam.exception;

import lombok.Data;

@Data
public class OnePieceException extends RuntimeException{

    private String messaggio;
    private String errore;
    private String codiceErrore;


    public OnePieceException(String messaggio, String errore, String codiceErrore) {
        this.messaggio = messaggio;
        this.errore = errore;
        this.codiceErrore = codiceErrore;
    }
}
