package com.tsm.ur.card.wiam.model.response;

import com.tsm.ur.card.wiam.entity.SealedOnePiece;

public record AggiungiSealedOPResponse
        (String messaggio,
         SealedOnePiece sealed){
}
