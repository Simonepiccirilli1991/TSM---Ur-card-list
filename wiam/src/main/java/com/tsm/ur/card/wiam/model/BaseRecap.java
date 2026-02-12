package com.tsm.ur.card.wiam.model;

import lombok.Data;

@Data
public class BaseRecap{

    // valori base carte
    private String id;
    private String usernameAssociato;
    private String nome;
    private String lingua;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private byte[] foto;

    // sezione gradata
    private Boolean gradata;
    private String enteGradazione;
    private String votoGradazione;
    // sezione stato carta
    private String statoCarta; // mint, near mint, excellent, good, light played,
    private String stato; // acquistato/venduto
    private String statoAcquisto; // disponibile/non disponibile
    // sezione vendita
    private Double prezzoVendita;
    private Double costiVendita;
    private Double netto;
    private String dataVendita;
    private String piattaformaVendita;
    private String note;

    // valori base sealed
    private String dataUscitaProdottoUfficiale;
    private String acquistatoPresso;

    // TipoProdotto, Pokemon o ONePiece
    private String tipoProdotto;
    // Tipo, carta o sealed
    private String tipo;
}
