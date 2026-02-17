package com.tsm.ur.card.seor.model.request;

import java.time.LocalDate;

public record AggiungiCartaOnePieceRequest(
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
    @Override
    public String toString() {
        return "AggiungiCartaOnePieceRequest[" +
                "usernameAssociato=" + usernameAssociato +
                ", nome=" + nome +
                ", lingua=" + lingua +
                ", espansione=" + espansione +
                ", prezzoAcquisto=" + prezzoAcquisto +
                ", dataAcquisto=" + dataAcquisto +
                ", foto=[MASKED]" +
                ", gradata=" + gradata +
                ", enteGradazione=" + enteGradazione +
                ", votoGradazione=" + votoGradazione +
                ", statoCarta=" + statoCarta +
                "]";
    }
}

