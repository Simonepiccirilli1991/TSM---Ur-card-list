package com.tsm.ur.card.frontend.model.form;

import lombok.Data;

@Data
public class AggiungiSealedOnePieceForm {
    private String nome;
    private String lingua;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private String dataUscitaProdottoUfficiale;
    private String foto;
    private String acquistatoPresso;
}

