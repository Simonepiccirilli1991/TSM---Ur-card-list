package com.tsm.ur.card.wiam.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("carta_pokemon")
@Data
public class CartaPokemon {

    @MongoId
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

    @Override
    public String toString() {
        return "CartaPokemon{" +
                "id='" + id + '\'' +
                ", usernameAssociato='" + usernameAssociato + '\'' +
                ", nome='" + nome + '\'' +
                ", lingua='" + lingua + '\'' +
                ", espansione='" + espansione + '\'' +
                ", prezzoAcquisto=" + prezzoAcquisto +
                ", dataAcquisto='" + dataAcquisto + '\'' +
                ", foto=[MASKED]" +
                ", gradata=" + gradata +
                ", enteGradazione='" + enteGradazione + '\'' +
                ", votoGradazione='" + votoGradazione + '\'' +
                ", statoCarta='" + statoCarta + '\'' +
                ", stato='" + stato + '\'' +
                ", statoAcquisto='" + statoAcquisto + '\'' +
                ", prezzoVendita=" + prezzoVendita +
                ", costiVendita=" + costiVendita +
                ", netto=" + netto +
                ", dataVendita='" + dataVendita + '\'' +
                ", piattaformaVendita='" + piattaformaVendita + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
