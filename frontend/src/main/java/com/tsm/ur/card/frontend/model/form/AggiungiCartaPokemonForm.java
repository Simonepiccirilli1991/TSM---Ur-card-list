package com.tsm.ur.card.frontend.model.form;

import lombok.Data;

@Data
public class AggiungiCartaPokemonForm {
    private String nome;
    private String lingua;
    private String espansione;
    private Double prezzoAcquisto;
    private String dataAcquisto;
    private String foto;
    private Boolean gradata = false;
    private String enteGradazione;
    private String votoGradazione;
    private String statoCarta;
}

