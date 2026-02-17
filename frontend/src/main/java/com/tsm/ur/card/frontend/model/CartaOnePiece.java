package com.tsm.ur.card.frontend.model;

import lombok.Data;

@Data
public class CartaOnePiece {
    private String id;
    private String usernameAssociato;
    private String nome;
    private String lingua;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private String foto;

    // Sezione stato
    private String statoCarta;
    private String stato;
    private String statoAcquisto;

    // Sezione vendita
    private Double prezzoVendita;
    private Double costiVendita;
    private Double netto;
    private String dataVendita;
}

