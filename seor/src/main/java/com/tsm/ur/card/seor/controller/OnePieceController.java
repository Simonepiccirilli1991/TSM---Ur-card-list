package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.dto.SealedOnePiece;
import com.tsm.ur.card.seor.model.request.AggiungiOnePieceSealedRequest;
import com.tsm.ur.card.seor.model.request.CancellaOnePieceSealedRequest;
import com.tsm.ur.card.seor.model.response.AggiungiSealedOPResponse;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.OnePieceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/onepiece")
public class OnePieceController {

    private final OnePieceService onePieceService;

    @PostMapping("/add-sealed")
    public Mono<ResponseEntity<AggiungiSealedOPResponse>> aggiungiSealedOP(@RequestBody AggiungiOnePieceSealedRequest request) {
        return onePieceService.aggiungiSealedOnePiece(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/delete-sealed")
    public Mono<ResponseEntity<BaseResponse>> cancellaSealedOP(@RequestBody CancellaOnePieceSealedRequest request) {
        return onePieceService.cancellaSealedOnePiece(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/get-sealed/{idSealed}")
    public Mono<ResponseEntity<SealedOnePiece>> getSealed(@PathVariable String idSealed) {
        return onePieceService.getSealedOnePieceById(idSealed)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/get-sealed-by-user/{username}")
    public Mono<ResponseEntity<List<SealedOnePiece>>> getSealedByUser(@PathVariable String username) {
        return onePieceService.getSealedOnePieceByUsername(username)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/get-sealed-bystato/{username}/{stato}")
    public Mono<ResponseEntity<List<SealedOnePiece>>> getSealedByStato(
            @PathVariable String username, @PathVariable String stato) {
        return onePieceService.getSealedOnePieceByUsernameAndStato(username, stato)
                .map(ResponseEntity::ok);
    }
}

