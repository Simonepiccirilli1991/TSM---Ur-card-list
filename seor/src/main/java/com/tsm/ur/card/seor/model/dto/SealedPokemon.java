package com.tsm.ur.card.seor.model.dto;

import lombok.Data;

@Data
public class SealedPokemon {

    private String id;
    private String usernameAssociato;
    private String nome;
    private String lingua;
    private String edizione;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private String dataUscitaProdottoUfficiale;
    private String stato;
    private String statoAcquisto;
    private byte[] foto;
    private String acquistatoPresso;
    // sezione vendita
    private Double prezzoVendita;
    private Double costiVendita;
    private Double netto;
    private String dataVendita;
    private String piattaformaVendita;
    private String note;
}

