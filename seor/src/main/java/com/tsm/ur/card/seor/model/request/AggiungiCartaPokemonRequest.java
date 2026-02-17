package com.tsm.ur.card.seor.model.request;

import java.time.LocalDate;

public record AggiungiCartaPokemonRequest(
        String usernameAssociato,
        String nome,
        String lingua,
        String espansione,
        Double prezzoAcquisto,
        LocalDate dataAcquisto,
        byte[] foto,
        Boolean gradata,
        String enteGradazione,
        String votoGradazione,
        String statoCarta
) {
}

