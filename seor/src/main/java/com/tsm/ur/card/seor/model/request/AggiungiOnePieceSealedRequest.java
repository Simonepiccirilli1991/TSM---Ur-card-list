package com.tsm.ur.card.seor.model.request;

import java.time.LocalDate;

public record AggiungiOnePieceSealedRequest(
        String username,
        String nome,
        String lingua,
        String espansione,
        Double prezzoAcquisto,
        LocalDate dataAcquisto,
        LocalDate dataUscitaProdottoUfficiale,
        byte[] foto,
        String acquistatoPresso
) {
    @Override
    public String toString() {
        return "AggiungiOnePieceSealedRequest[" +
                "username=" + username +
                ", nome=" + nome +
                ", lingua=" + lingua +
                ", espansione=" + espansione +
                ", prezzoAcquisto=" + prezzoAcquisto +
                ", dataAcquisto=" + dataAcquisto +
                ", dataUscitaProdottoUfficiale=" + dataUscitaProdottoUfficiale +
                ", foto=[MASKED]" +
                ", acquistatoPresso=" + acquistatoPresso +
                "]";
    }
}

