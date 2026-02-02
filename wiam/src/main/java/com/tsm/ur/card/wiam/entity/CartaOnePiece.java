package com.tsm.ur.card.wiam.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document("carta_one_piece")
public class CartaOnePiece {

    @MongoId
    private String id;
    private String emailAssociated;
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
}
