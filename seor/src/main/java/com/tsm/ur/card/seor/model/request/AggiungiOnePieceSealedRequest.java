package com.tsm.ur.card.seor.model.request;

import java.time.LocalDateTime;

public record AggiungiOnePieceSealedRequest(
        String username,
        String nome,
        String linguea,
        String espansione,
        Double prezzoAcquisto,
        LocalDateTime dataAcquisto,
        LocalDateTime dataUscitaProdottoUfficiale,
        byte[] foto,
        String acquistatoPresso
) {
}

