package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.dto.CartaPokemon;
import com.tsm.ur.card.seor.model.dto.SealedPokemon;
import com.tsm.ur.card.seor.model.request.AggiungiCartaPokemonRequest;
import com.tsm.ur.card.seor.model.request.AggiungiPokemonSealedRequest;
import com.tsm.ur.card.seor.model.request.CancellaCartaPokemonRequest;
import com.tsm.ur.card.seor.model.request.CancellaPokemonSealedRequest;
import com.tsm.ur.card.seor.model.response.AggiungiCartaPokemonResponse;
import com.tsm.ur.card.seor.model.response.AggiungiSealedPkmResponse;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonService {

    private final WiamIntegration wiamIntegration;

    // ===================== POKEMON CARD APIs =====================

    public Mono<AggiungiCartaPokemonResponse> aggiungiCartaPokemon(AggiungiCartaPokemonRequest request) {
        log.info("PokemonService - aggiungiCartaPokemon per username: {}", request.usernameAssociato());
        return wiamIntegration.aggiungiCartaPokemon(request);
    }

    public Mono<BaseResponse> cancellaCartaPokemon(CancellaCartaPokemonRequest request) {
        log.info("PokemonService - cancellaCartaPokemon per idCarta: {}", request.idCarta());
        return wiamIntegration.cancellaCartaPokemon(request);
    }

    public Mono<CartaPokemon> getCartaPokemonById(String idCarta) {
        log.info("PokemonService - getCartaPokemonById per id: {}", idCarta);
        return wiamIntegration.getCartaPokemonById(idCarta);
    }

    public Mono<List<CartaPokemon>> getCartePokemonByUsername(String username) {
        log.info("PokemonService - getCartePokemonByUsername per username: {}", username);
        return wiamIntegration.getCartePokemonByUsername(username);
    }

    public Mono<List<CartaPokemon>> getCartePokemonByUsernameAndStato(String username, String stato) {
        log.info("PokemonService - getCartePokemonByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamIntegration.getCartePokemonByUsernameAndStato(username, stato);
    }

    // ===================== POKEMON SEALED APIs =====================

    public Mono<AggiungiSealedPkmResponse> aggiungiSealedPokemon(AggiungiPokemonSealedRequest request) {
        log.info("PokemonService - aggiungiSealedPokemon per username: {}", request.username());
        return wiamIntegration.aggiungiSealedPokemon(request);
    }

    public Mono<BaseResponse> cancellaSealedPokemon(CancellaPokemonSealedRequest request) {
        log.info("PokemonService - cancellaSealedPokemon per idSealed: {}", request.idSealed());
        return wiamIntegration.cancellaSealedPokemon(request);
    }

    public Mono<SealedPokemon> getSealedPokemonById(String idSealed) {
        log.info("PokemonService - getSealedPokemonById per id: {}", idSealed);
        return wiamIntegration.getSealedPokemonById(idSealed);
    }

    public Mono<List<SealedPokemon>> getSealedPokemonByUsername(String username) {
        log.info("PokemonService - getSealedPokemonByUsername per username: {}", username);
        return wiamIntegration.getSealedPokemonByUsername(username);
    }

    public Mono<List<SealedPokemon>> getSealedPokemonByUsernameAndStato(String username, String stato) {
        log.info("PokemonService - getSealedPokemonByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamIntegration.getSealedPokemonByUsernameAndStato(username, stato);
    }
}

