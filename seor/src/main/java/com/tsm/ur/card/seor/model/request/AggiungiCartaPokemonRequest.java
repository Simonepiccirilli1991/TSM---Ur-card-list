package com.tsm.ur.card.seor.model.request;

import java.time.LocalDateTime;

public record AggiungiCartaPokemonRequest(
        String usernameAssociato,
        String nome,
        String lingua,
        String espansione,
        Double prezzoAcquisto,
        LocalDateTime dataAcquisto,
        byte[] foto,
        Boolean gradata,
        String enteGradazione,
        String votoGradazione,
        String statoCarta
) {
}

