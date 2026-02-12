package com.tsm.ur.card.wiam.controller;


import com.tsm.ur.card.wiam.entity.CartaPokemon;
import com.tsm.ur.card.wiam.entity.SealedPokemon;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.AggiungiCartaPokemonRequest;
import com.tsm.ur.card.wiam.model.request.AggiungiPokemonSealedRequest;
import com.tsm.ur.card.wiam.model.request.CancellaCartaPokemonRequest;
import com.tsm.ur.card.wiam.model.request.CancellaPokemonSealedRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiCartaPokemonResponse;
import com.tsm.ur.card.wiam.model.response.AggiungiSealedPkmResponse;
import com.tsm.ur.card.wiam.service.carte.PokemonCardService;
import com.tsm.ur.card.wiam.service.carte.PokemonSealedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/pokemon/card")
@RequiredArgsConstructor
public class PokemonController {


    private final PokemonCardService pokemonCardService;
    private final PokemonSealedService pokemonSealedService;


    // carteSingole
    @PostMapping("addcard")
    public ResponseEntity<AggiungiCartaPokemonResponse> aggiungiCartaPokemon(@RequestBody AggiungiCartaPokemonRequest request){
        return ResponseEntity.ok(pokemonCardService.aggiungiCartaPokemonResponse(request));
    }

    @DeleteMapping("cancellacarta")
    public ResponseEntity<BaseResponse> cancellaCartaPokemon(@RequestBody CancellaCartaPokemonRequest request){
        return ResponseEntity.ok(pokemonCardService.cancellaCartaPokemonById(request));
    }

    @GetMapping("getcard/{idCarta}")
    public ResponseEntity<CartaPokemon> getCartaPokemonById(@PathVariable("idCarta") String idCarta) {
        return ResponseEntity.ok(pokemonCardService.getCartaPokemonById(idCarta));
    }

    @GetMapping("getcardsbyusername/{username}")
    public ResponseEntity<Iterable<CartaPokemon>> getCartePokemonByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(pokemonCardService.getCartePokemonByUsername(username));
    }

    @GetMapping("getcardbyUsernameandstato/{username}/{stato}")
    public ResponseEntity<Iterable<CartaPokemon>> getCartePokemonByUsernameAndStato(@PathVariable("username") String username, @PathVariable("stato") String stato) {
        return ResponseEntity.ok(pokemonCardService.getCartePokemonByUsernameAndStatoAcquisto(username, stato));
    }

    // sealed
    @PostMapping("addsealed")
    public ResponseEntity<AggiungiSealedPkmResponse> aggiungiSealedPokemon(@RequestBody AggiungiPokemonSealedRequest request){
        return ResponseEntity.ok(pokemonSealedService.aggiungiSealedPokm(request));
    }

    @DeleteMapping("cancellasealed")
    public ResponseEntity<BaseResponse> cancellaSealedPokemon(@RequestBody CancellaPokemonSealedRequest request) {
        return ResponseEntity.ok(pokemonSealedService.cancellaSealedPokemonById(request));
    }

    @GetMapping("getsealedbyid/{idSealed}")
    public ResponseEntity<SealedPokemon> getSealedPokemonById(@PathVariable("idSealed") String idSealed) {
        return ResponseEntity.ok(pokemonSealedService.getSealedPokemonById(idSealed));
    }

    @GetMapping("getSealedByUsername/{username}")
    public ResponseEntity<Iterable<SealedPokemon>> getSealedPokemonByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(pokemonSealedService.getSealedPokemonByUsername(username));
    }

    @GetMapping("getSealedByUsernameAndStato/{username}/{stato}")
    public ResponseEntity<Iterable<SealedPokemon>> getSealedPokemonByUsernameAndStato(@PathVariable("username") String username, @PathVariable("stato") String stato) {
        return ResponseEntity.ok(pokemonSealedService.getSealedPokemonByUsernameAndStatoAcquisto(username, stato));
    }
}
