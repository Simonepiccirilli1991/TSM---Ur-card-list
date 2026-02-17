package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.dto.CartaPokemon;
import com.tsm.ur.card.seor.model.dto.SealedPokemon;
import com.tsm.ur.card.seor.model.request.AggiungiCartaPokemonRequest;
import com.tsm.ur.card.seor.model.request.AggiungiPokemonSealedRequest;
import com.tsm.ur.card.seor.model.response.AggiungiCartaPokemonResponse;
import com.tsm.ur.card.seor.model.response.AggiungiSealedPkmResponse;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonService {

    private final WiamIntegration wiamIntegration;

    // ===================== POKEMON CARD APIs =====================

    public AggiungiCartaPokemonResponse aggiungiCartaPokemon(String username, AggiungiCartaPokemonRequest request) {
        log.info("PokemonService - aggiungiCartaPokemon per username (JWT): {}", username);
        return wiamIntegration.aggiungiCartaPokemon(username, request);
    }

    public BaseResponse cancellaCartaPokemon(String username, String idCarta) {
        log.info("PokemonService - cancellaCartaPokemon per username (JWT): {}, idCarta: {}", username, idCarta);
        return wiamIntegration.cancellaCartaPokemon(username, idCarta);
    }

    public CartaPokemon getCartaPokemonById(String idCarta) {
        log.info("PokemonService - getCartaPokemonById per id: {}", idCarta);
        return wiamIntegration.getCartaPokemonById(idCarta);
    }

    public List<CartaPokemon> getCartePokemonByUsername(String username) {
        log.info("PokemonService - getCartePokemonByUsername per username: {}", username);
        return wiamIntegration.getCartePokemonByUsername(username);
    }

    public List<CartaPokemon> getCartePokemonByUsernameAndStato(String username, String stato) {
        log.info("PokemonService - getCartePokemonByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamIntegration.getCartePokemonByUsernameAndStato(username, stato);
    }

    // ===================== POKEMON SEALED APIs =====================

    public AggiungiSealedPkmResponse aggiungiSealedPokemon(String username, AggiungiPokemonSealedRequest request) {
        log.info("PokemonService - aggiungiSealedPokemon per username (JWT): {}", username);
        return wiamIntegration.aggiungiSealedPokemon(username, request);
    }

    public BaseResponse cancellaSealedPokemon(String username, String idSealed) {
        log.info("PokemonService - cancellaSealedPokemon per username (JWT): {}, idSealed: {}", username, idSealed);
        return wiamIntegration.cancellaSealedPokemon(username, idSealed);
    }

    public SealedPokemon getSealedPokemonById(String idSealed) {
        log.info("PokemonService - getSealedPokemonById per id: {}", idSealed);
        return wiamIntegration.getSealedPokemonById(idSealed);
    }

    public List<SealedPokemon> getSealedPokemonByUsername(String username) {
        log.info("PokemonService - getSealedPokemonByUsername per username: {}", username);
        return wiamIntegration.getSealedPokemonByUsername(username);
    }

    public List<SealedPokemon> getSealedPokemonByUsernameAndStato(String username, String stato) {
        log.info("PokemonService - getSealedPokemonByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamIntegration.getSealedPokemonByUsernameAndStato(username, stato);
    }
}

