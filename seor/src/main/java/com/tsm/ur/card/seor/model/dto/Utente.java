package com.tsm.ur.card.seor.model.dto;

import lombok.Data;

@Data
public class Utente {

    private String email;
    private String password;
    private String username;
    private String dataRegistrazione;
    private Boolean enrollment;
    private Integer otpCount;
    private String otpLastTime;
}

