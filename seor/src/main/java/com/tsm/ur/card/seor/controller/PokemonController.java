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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("api/v1/pokemon/card")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    // ===================== POKEMON CARD APIs =====================

    @PostMapping("/addcard")
    public ResponseEntity<AggiungiCartaPokemonResponse> aggiungiCartaPokemon(@RequestBody AggiungiCartaPokemonRequest request) {
        return  ResponseEntity.ok(pokemonService.aggiungiCartaPokemon(request));
    }

    @DeleteMapping("/cancellacarta")
    public ResponseEntity<BaseResponse> cancellaCartaPokemon(@RequestBody CancellaCartaPokemonRequest request) {
        return  ResponseEntity.ok(pokemonService.cancellaCartaPokemon(request));
    }

    @GetMapping("/getcard/{idCarta}")
    public ResponseEntity<CartaPokemon> getCartaPokemonById(@PathVariable String idCarta) {
        return  ResponseEntity.ok(pokemonService.getCartaPokemonById(idCarta));
    }

    @GetMapping("/getcardsbyusername/{username}")
    public ResponseEntity<List<CartaPokemon>> getCartePokemonByUsername(@PathVariable String username) {
        return  ResponseEntity.ok(pokemonService.getCartePokemonByUsername(username));
    }

    @GetMapping("/getcardbyUsernameandstato/{username}/{stato}")
    public ResponseEntity<List<CartaPokemon>> getCartePokemonByUsernameAndStato(
            @PathVariable String username, @PathVariable String stato) {
        return  ResponseEntity.ok(pokemonService.getCartePokemonByUsernameAndStato(username, stato));
    }

    // ===================== POKEMON SEALED APIs =====================

    @PostMapping("/addsealed")
    public ResponseEntity<AggiungiSealedPkmResponse> aggiungiSealedPokemon(@RequestBody AggiungiPokemonSealedRequest request) {
        return  ResponseEntity.ok(pokemonService.aggiungiSealedPokemon(request));
    }

    @DeleteMapping("/cancellasealed")
    public ResponseEntity<BaseResponse> cancellaSealedPokemon(@RequestBody CancellaPokemonSealedRequest request) {
        return  ResponseEntity.ok(pokemonService.cancellaSealedPokemon(request));
    }

    @GetMapping("/getsealedbyid/{idSealed}")
    public ResponseEntity<SealedPokemon> getSealedPokemonById(@PathVariable String idSealed) {
        return  ResponseEntity.ok(pokemonService.getSealedPokemonById(idSealed));
    }

    @GetMapping("/getSealedByUsername/{username}")
    public ResponseEntity<List<SealedPokemon>> getSealedPokemonByUsername(@PathVariable String username) {
        return  ResponseEntity.ok(pokemonService.getSealedPokemonByUsername(username));
    }

    @GetMapping("/getSealedByUsernameAndStato/{username}/{stato}")
    public ResponseEntity<List<SealedPokemon>> getSealedPokemonByUsernameAndStato(
            @PathVariable String username, @PathVariable String stato) {
        return  ResponseEntity.ok(pokemonService.getSealedPokemonByUsernameAndStato(username, stato));
    }
}

