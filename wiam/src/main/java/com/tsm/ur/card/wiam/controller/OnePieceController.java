package com.tsm.ur.card.wiam.controller;

import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.AggiungiOnePiceSealedRequest;
import com.tsm.ur.card.wiam.model.request.CancellaOpSealedRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiSealedOPResponse;
import com.tsm.ur.card.wiam.service.carte.OpSealedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/onepiece")
public class OnePieceController {


    private final OpSealedService opSealedService;

    // ONE PIECE SEALED APIs
    @PostMapping("/add-sealed")
    public ResponseEntity<AggiungiSealedOPResponse> aggiungiSealedOP(@RequestBody AggiungiOnePiceSealedRequest request) {
        return ResponseEntity.ok(opSealedService.aggiungiSealedOnePiece(request));
    }

    @PostMapping("/delete-sealed")
    public ResponseEntity<BaseResponse> cancellaSealedOP(@RequestBody CancellaOpSealedRequest request) {
        return ResponseEntity.ok(opSealedService.cancellaSealedOnePiece(request));
    }

    @GetMapping("/get-sealed/{idSealed}")
    public  ResponseEntity<SealedOnePiece> getSealed(@PathVariable String idSealed) {
        return ResponseEntity.ok(opSealedService.getSealedOnePieceById(idSealed));
    }

    @GetMapping("/get-sealed-by-user/{username}")
    public  ResponseEntity<List<SealedOnePiece>> getSealedByUser(@PathVariable String username) {
        return ResponseEntity.ok(opSealedService.getSealedOnePieceByUsername(username));
    }

    @GetMapping("/get-sealed-bystato/{username}/{stato}")
    public  ResponseEntity<List<SealedOnePiece>> getSealedByStato(@PathVariable String username, @PathVariable String stato) {
        return ResponseEntity.ok(opSealedService.getSealedOnePieceByUsernameAndStatoAcquisto(username, stato));
    }

    // ONE PIECE CARD APIs
}
