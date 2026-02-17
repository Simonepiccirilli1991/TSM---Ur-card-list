package com.tsm.ur.card.frontend.model;

import lombok.Data;

@Data
public class BaseRecap {
    private String id;
    private String usernameAssociato;
    private String nome;
    private String lingua;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private String foto;

    // Sezione gradazione
    private Boolean gradata;
    private String enteGradazione;
    private String votoGradazione;

    // Sezione stato
    private String statoCarta;
    private String stato;
    private String statoAcquisto;

    // Sezione vendita
    private Double prezzoVendita;
    private Double costiVendita;
    private Double netto;
    private String dataVendita;
    private String piattaformaVendita;
    private String note;

    // Metadata
    private String tipoProdotto;
}

