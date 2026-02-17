package com.tsm.ur.card.authserver.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("utente")
@Data
public class Utente {

    @MongoId
    private String email;
    private String password;
    private String username;
    private String dataRegistrazione;
    private Boolean enrollment;
    private Integer otpCount;
    private String otpLastTime;
}

