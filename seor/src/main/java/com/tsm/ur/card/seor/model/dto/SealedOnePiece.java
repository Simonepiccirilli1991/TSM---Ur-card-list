package com.tsm.ur.card.seor.model.dto;

import lombok.Data;

@Data
public class SealedOnePiece {

    private String id;
    private String usernameAssociato;
    private String nome;
    private String lingua;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private String dataUscitaProdottoUfficiale;
    private String acquistatoPresso;
    private String stato;
    private String statoAcquisto;
    private byte[] foto;

    // sezione vendita
    private Double prezzoVendita;
    private Double costiVendita;
    private Double netto;
    private String dataVendita;
    private String piattaformaVendita;
    private String note;

    @Override
    public String toString() {
        return "SealedOnePiece{" +
                "id='" + id + '\'' +
                ", usernameAssociato='" + usernameAssociato + '\'' +
                ", nome='" + nome + '\'' +
                ", lingua='" + lingua + '\'' +
                ", espansione='" + espansione + '\'' +
                ", prezzoAcquisto=" + prezzoAcquisto +
                ", dataAcquisto='" + dataAcquisto + '\'' +
                ", dataUscitaProdottoUfficiale='" + dataUscitaProdottoUfficiale + '\'' +
                ", acquistatoPresso='" + acquistatoPresso + '\'' +
                ", stato='" + stato + '\'' +
                ", statoAcquisto='" + statoAcquisto + '\'' +
                ", foto=[MASKED]" +
                ", prezzoVendita=" + prezzoVendita +
                ", costiVendita=" + costiVendita +
                ", netto=" + netto +
                ", dataVendita='" + dataVendita + '\'' +
                ", piattaformaVendita='" + piattaformaVendita + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}

