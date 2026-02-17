package com.tsm.ur.card.wiam.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document("sealed_pokemon")
public class SealedPokemon {

    @MongoId
    private String id;
    private String usernameAssociato;
    private String nome;
    private String lingua;
    private String edizione;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private String dataUscitaProdottoUfficiale;
    private String stato; // acquistato/venduto
    private String statoAcquisto; // disponibile/non disponibile
    private byte[] foto;
    private String acquistatoPresso;
    // sezione vendita
    private Double prezzoVendita;
    private Double costiVendita;
    private Double netto;
    private String dataVendita;
    private String piattaformaVendita;
    private String note;

    @Override
    public String toString() {
        return "SealedPokemon{" +
                "id='" + id + '\'' +
                ", usernameAssociato='" + usernameAssociato + '\'' +
                ", nome='" + nome + '\'' +
                ", lingua='" + lingua + '\'' +
                ", edizione='" + edizione + '\'' +
                ", espansione='" + espansione + '\'' +
                ", prezzoAcquisto=" + prezzoAcquisto +
                ", dataAcquisto='" + dataAcquisto + '\'' +
                ", dataUscitaProdottoUfficiale='" + dataUscitaProdottoUfficiale + '\'' +
                ", stato='" + stato + '\'' +
                ", statoAcquisto='" + statoAcquisto + '\'' +
                ", foto=[MASKED]" +
                ", acquistatoPresso='" + acquistatoPresso + '\'' +
                ", prezzoVendita=" + prezzoVendita +
                ", costiVendita=" + costiVendita +
                ", netto=" + netto +
                ", dataVendita='" + dataVendita + '\'' +
                ", piattaformaVendita='" + piattaformaVendita + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
