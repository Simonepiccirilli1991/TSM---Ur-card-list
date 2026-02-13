package com.tsm.ur.card.seor.model.request;

public record RegistraUtenteRequest(
        String username,
        String password,
        String email
) {
}

