package com.tsm.ur.card.frontend.model;

import lombok.Data;

@Data
public class DashboardStats {
    private double valoreCollezione;
    private double profittoTotale;
    private double totaleAcquisti;
    private double totaleVendite;
    private int totaleProdotti;
    private int prodottiInCollezione;
    private int prodottiInVendita;
    private int prodottiVenduti;
    private int numeroPokemonCards;
    private int numeroPokemonSealed;
    private int numeroOnePieceCards;
    private int numeroOnePieceSealed;
}

