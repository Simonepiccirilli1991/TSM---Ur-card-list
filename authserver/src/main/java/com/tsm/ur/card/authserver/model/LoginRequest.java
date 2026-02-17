package com.tsm.ur.card.authserver.model;

public record LoginRequest(
        String username,
        String password
) {
}

