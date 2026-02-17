package com.tsm.ur.card.seor.model.response;

import com.tsm.ur.card.seor.model.dto.CartaOnePiece;

public record AggiungiCartaOnePieceResponse(
        String message,
        CartaOnePiece cartaOnePiece
) {
}

