package com.tsm.ur.card.wiam.except;

import lombok.Data;

@Data
public class PokemonException extends RuntimeException{

    private String messaggio;
    private String errore;
    private String codiceErrore;


    public PokemonException(String messaggio, String errore, String codiceErrore) {
        this.messaggio = messaggio;
        this.errore = errore;
        this.codiceErrore = codiceErrore;
    }
}
