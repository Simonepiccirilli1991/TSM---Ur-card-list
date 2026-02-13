package com.tsm.ur.card.seor.model.response;

import com.tsm.ur.card.seor.model.dto.SealedOnePiece;

public record AggiungiSealedOPResponse(
        String messaggio,
        SealedOnePiece sealed
) {
}

