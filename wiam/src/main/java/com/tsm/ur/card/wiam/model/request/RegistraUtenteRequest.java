package com.tsm.ur.card.wiam.model.request;

public record RegistraUtenteRequest(
        String username,
        String password,
        String email) {
}
