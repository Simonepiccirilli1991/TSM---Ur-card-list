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
    public ResponseEntity<AggiungiSealedOPResponse> aggiungiSealedOP(@RequestBody AggiungiOnePieceSealedRequest request) {
        return ResponseEntity.ok(onePieceService.aggiungiSealedOnePiece(request));
    }

    @PostMapping("/delete-sealed")
    public ResponseEntity<BaseResponse> cancellaSealedOP(@RequestBody CancellaOnePieceSealedRequest request) {
        return  ResponseEntity.ok(onePieceService.cancellaSealedOnePiece(request));
    }

    @GetMapping("/get-sealed/{idSealed}")
    public ResponseEntity<SealedOnePiece> getSealed(@PathVariable String idSealed) {
        return  ResponseEntity.ok(onePieceService.getSealedOnePieceById(idSealed));
    }

    @GetMapping("/get-sealed-by-user/{username}")
    public ResponseEntity<List<SealedOnePiece>> getSealedByUser(@PathVariable String username) {
        return  ResponseEntity.ok(onePieceService.getSealedOnePieceByUsername(username));
    }

    @GetMapping("/get-sealed-bystato/{username}/{stato}")
    public ResponseEntity<List<SealedOnePiece>> getSealedByStato(
            @PathVariable String username, @PathVariable String stato) {
        return  ResponseEntity.ok(onePieceService.getSealedOnePieceByUsernameAndStato(username, stato));
    }
}

