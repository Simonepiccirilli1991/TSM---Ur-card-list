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
    public Mono<ResponseEntity<AggiungiCartaPokemonResponse>> aggiungiCartaPokemon(@RequestBody AggiungiCartaPokemonRequest request) {
        return pokemonService.aggiungiCartaPokemon(request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/cancellacarta")
    public Mono<ResponseEntity<BaseResponse>> cancellaCartaPokemon(@RequestBody CancellaCartaPokemonRequest request) {
        return pokemonService.cancellaCartaPokemon(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/getcard/{idCarta}")
    public Mono<ResponseEntity<CartaPokemon>> getCartaPokemonById(@PathVariable String idCarta) {
        return pokemonService.getCartaPokemonById(idCarta)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/getcardsbyusername/{username}")
    public Mono<ResponseEntity<List<CartaPokemon>>> getCartePokemonByUsername(@PathVariable String username) {
        return pokemonService.getCartePokemonByUsername(username)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/getcardbyUsernameandstato/{username}/{stato}")
    public Mono<ResponseEntity<List<CartaPokemon>>> getCartePokemonByUsernameAndStato(
            @PathVariable String username, @PathVariable String stato) {
        return pokemonService.getCartePokemonByUsernameAndStato(username, stato)
                .map(ResponseEntity::ok);
    }

    // ===================== POKEMON SEALED APIs =====================

    @PostMapping("/addsealed")
    public Mono<ResponseEntity<AggiungiSealedPkmResponse>> aggiungiSealedPokemon(@RequestBody AggiungiPokemonSealedRequest request) {
        return pokemonService.aggiungiSealedPokemon(request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/cancellasealed")
    public Mono<ResponseEntity<BaseResponse>> cancellaSealedPokemon(@RequestBody CancellaPokemonSealedRequest request) {
        return pokemonService.cancellaSealedPokemon(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/getsealedbyid/{idSealed}")
    public Mono<ResponseEntity<SealedPokemon>> getSealedPokemonById(@PathVariable String idSealed) {
        return pokemonService.getSealedPokemonById(idSealed)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/getSealedByUsername/{username}")
    public Mono<ResponseEntity<List<SealedPokemon>>> getSealedPokemonByUsername(@PathVariable String username) {
        return pokemonService.getSealedPokemonByUsername(username)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/getSealedByUsernameAndStato/{username}/{stato}")
    public Mono<ResponseEntity<List<SealedPokemon>>> getSealedPokemonByUsernameAndStato(
            @PathVariable String username, @PathVariable String stato) {
        return pokemonService.getSealedPokemonByUsernameAndStato(username, stato)
                .map(ResponseEntity::ok);
    }
}

