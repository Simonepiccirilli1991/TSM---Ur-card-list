package com.tsm.ur.card.wiam.except;

import lombok.Data;

@Data
public class UtenteException extends RuntimeException{

    private String errore;
    private String codice;
    private String messaggio;

    public UtenteException(String errore, String codice, String messaggio) {
        this.errore = errore;
        this.codice = codice;
        this.messaggio = messaggio;
    }
}
