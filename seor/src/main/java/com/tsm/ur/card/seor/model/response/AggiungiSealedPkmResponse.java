package com.tsm.ur.card.seor.model.response;

import com.tsm.ur.card.seor.model.dto.SealedPokemon;

public record AggiungiSealedPkmResponse(
        String messaggio,
        SealedPokemon sealedPokemon
) {
}

