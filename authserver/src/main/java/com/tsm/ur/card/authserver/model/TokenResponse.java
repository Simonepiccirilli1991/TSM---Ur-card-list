package com.tsm.ur.card.authserver.model;

public record TokenResponse(
        String accessToken,
        String tokenType,
        String message,
        Long expiresIn
) {
}

