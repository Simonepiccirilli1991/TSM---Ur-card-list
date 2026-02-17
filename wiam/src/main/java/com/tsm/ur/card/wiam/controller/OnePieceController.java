package com.tsm.ur.card.wiam.controller;

import com.tsm.ur.card.wiam.entity.CartaOnePiece;
import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.AggiungiOnePiceSealedRequest;
import com.tsm.ur.card.wiam.model.request.AggiungiOnePieceCardRequest;
import com.tsm.ur.card.wiam.model.request.CancellaCartaOpRequest;
import com.tsm.ur.card.wiam.model.request.CancellaOpSealedRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiCartaOPResponse;
import com.tsm.ur.card.wiam.model.response.AggiungiSealedOPResponse;
import com.tsm.ur.card.wiam.service.carte.OpCardService;
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
    private final OpCardService opCardService;

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
    @PostMapping("/add-card")
    public ResponseEntity<AggiungiCartaOPResponse> aggiungiCartaOP(@RequestBody AggiungiOnePieceCardRequest request) {
        return ResponseEntity.ok(opCardService.aggiungiCartaOP(request));
    }

    @PostMapping("/delete-card")
    public ResponseEntity<BaseResponse> cancellaCartaOP(@RequestBody CancellaCartaOpRequest request) {
        return ResponseEntity.ok(opCardService.cancellaCartaOP(request));
    }

    @GetMapping("/get-card/{idCarta}")
    public ResponseEntity<CartaOnePiece> getCard(@PathVariable String idCarta) {
        return ResponseEntity.ok(opCardService.getCartaOPById(idCarta));
    }

    @GetMapping("/get-cards-by-user/{username}")
    public ResponseEntity<List<CartaOnePiece>> getCardsByUser(@PathVariable String username) {
        return ResponseEntity.ok(opCardService.getCartaOPByUsername(username));
    }

    @GetMapping("/get-cards-bystato/{username}/{stato}")
    public ResponseEntity<List<CartaOnePiece>> getCardsByStato(@PathVariable String username, @PathVariable String stato) {
        return ResponseEntity.ok(opCardService.getCartaOpByStatoAndUsername(stato, username));
    }
}
