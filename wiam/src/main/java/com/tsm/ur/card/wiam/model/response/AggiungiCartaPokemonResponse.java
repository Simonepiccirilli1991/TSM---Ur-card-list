package com.tsm.ur.card.wiam.model.response;

import com.tsm.ur.card.wiam.entity.CartaPokemon;

public record AggiungiCartaPokemonResponse(
        String message,
        CartaPokemon cartaPokemon
) {
}
