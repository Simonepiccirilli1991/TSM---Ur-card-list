package com.tsm.ur.card.seor.model.dto;

import lombok.Data;

@Data
public class CartaPokemon {

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
    private String statoCarta;
    private String stato;
    private String statoAcquisto;
    // sezione vendita
    private Double prezzoVendita;
    private Double costiVendita;
    private Double netto;
    private String dataVendita;
    private String piattaformaVendita;
    private String note;
}

