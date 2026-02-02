package com.tsm.ur.card.wiam.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("utente")
@Data
public class Utente {


    @MongoId
    private String email;
    private String password;

    @Indexed(unique = true) // rende il parametro univoco
    private String username;
    private String dataRegistrazione;
    private Boolean enrollment;
    private Integer otpCount;
    private String otpLastTime;

}
