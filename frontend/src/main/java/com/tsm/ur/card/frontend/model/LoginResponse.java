package com.tsm.ur.card.frontend.model;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private String message;
    private Long expiresIn;
}

