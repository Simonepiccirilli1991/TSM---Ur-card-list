package com.tsm.ur.card.seor.model.response;

import com.tsm.ur.card.seor.model.dto.CartaPokemon;

public record AggiungiCartaPokemonResponse(
        String message,
        CartaPokemon cartaPokemon
) {
}

