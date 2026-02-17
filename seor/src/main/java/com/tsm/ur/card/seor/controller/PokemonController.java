package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.dto.CartaPokemon;
import com.tsm.ur.card.seor.model.dto.SealedPokemon;
import com.tsm.ur.card.seor.model.request.AggiungiCartaPokemonRequest;
import com.tsm.ur.card.seor.model.request.AggiungiPokemonSealedRequest;
import com.tsm.ur.card.seor.model.request.CancellaCartaPokemonRequest;
import com.tsm.ur.card.seor.model.request.CancellaPokemonSealedRequest;
import com.tsm.ur.card.seor.model.response.AggiungiCartaPokemonResponse;
import com.tsm.ur.card.seor.model.response.AggiungiSealedPkmResponse;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.PokemonService;
import com.tsm.ur.card.seor.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pokemon/card")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;
    private final JwtUtils jwtUtils;

    // ===================== POKEMON CARD APIs =====================

    @PostMapping("/addcard")
    public ResponseEntity<AggiungiCartaPokemonResponse> aggiungiCartaPokemon(
            @RequestBody AggiungiCartaPokemonRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(pokemonService.aggiungiCartaPokemon(username, request));
    }

    @DeleteMapping("/cancellacarta")
    public ResponseEntity<BaseResponse> cancellaCartaPokemon(
            @RequestBody CancellaCartaPokemonRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(pokemonService.cancellaCartaPokemon(username, request.idCarta()));
    }

    @GetMapping("/getcard/{idCarta}")
    public ResponseEntity<CartaPokemon> getCartaPokemonById(@PathVariable String idCarta) {
        return ResponseEntity.ok(pokemonService.getCartaPokemonById(idCarta));
    }

    @GetMapping("/getcardsbyusername")
    public ResponseEntity<List<CartaPokemon>> getCartePokemonByUsername(Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(pokemonService.getCartePokemonByUsername(username));
    }

    @GetMapping("/getcardbyUsernameandstato/{stato}")
    public ResponseEntity<List<CartaPokemon>> getCartePokemonByUsernameAndStato(
            @PathVariable String stato,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(pokemonService.getCartePokemonByUsernameAndStato(username, stato));
    }

    // ===================== POKEMON SEALED APIs =====================

    @PostMapping("/addsealed")
    public ResponseEntity<AggiungiSealedPkmResponse> aggiungiSealedPokemon(
            @RequestBody AggiungiPokemonSealedRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(pokemonService.aggiungiSealedPokemon(username, request));
    }

    @DeleteMapping("/cancellasealed")
    public ResponseEntity<BaseResponse> cancellaSealedPokemon(
            @RequestBody CancellaPokemonSealedRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(pokemonService.cancellaSealedPokemon(username, request.idSealed()));
    }

    @GetMapping("/getsealedbyid/{idSealed}")
    public ResponseEntity<SealedPokemon> getSealedPokemonById(@PathVariable String idSealed) {
        return ResponseEntity.ok(pokemonService.getSealedPokemonById(idSealed));
    }

    @GetMapping("/getSealedByUsername")
    public ResponseEntity<List<SealedPokemon>> getSealedPokemonByUsername(Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(pokemonService.getSealedPokemonByUsername(username));
    }

    @GetMapping("/getSealedByUsernameAndStato/{stato}")
    public ResponseEntity<List<SealedPokemon>> getSealedPokemonByUsernameAndStato(
            @PathVariable String stato,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(pokemonService.getSealedPokemonByUsernameAndStato(username, stato));
    }
}

