package com.tsm.ur.card.frontend.model.form;

import lombok.Data;

@Data
public class AggiungiCartaOnePieceForm {
    private String nome;
    private String lingua;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private String foto;
    private String statoCarta;
}

