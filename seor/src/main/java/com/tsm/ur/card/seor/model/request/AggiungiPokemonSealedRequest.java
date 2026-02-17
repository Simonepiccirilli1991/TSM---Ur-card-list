package com.tsm.ur.card.seor.model.request;

import java.time.LocalDate;

public record AggiungiPokemonSealedRequest(
        String username,
        String nome,
        String linguea,
        String espansione,
        Double prezzoAcquisto,
        LocalDate dataAcquisto,
        LocalDate dataUscitaProdottoUfficiale,
        byte[] foto,
        String acquistatoPresso
) {
}
