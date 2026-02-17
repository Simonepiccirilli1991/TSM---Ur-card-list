package com.tsm.ur.card.wiam.model.request;

import java.time.LocalDate;

public record VendiProdottoRequest(
        String idProdotto,
        String username,
        String tipoProdotto, // POKEMON_CARD, POKEMON_SEALED, ONEPIECE_CARD, ONEPIECE_SEALED
        Double prezzoVendita,
        Double costiVendita,
        LocalDate dataVendita,
        String piattaformaVendita,
        String note
) {
}

