package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.dto.CartaOnePiece;
import com.tsm.ur.card.seor.model.dto.SealedOnePiece;
import com.tsm.ur.card.seor.model.request.AggiungiCartaOnePieceRequest;
import com.tsm.ur.card.seor.model.request.AggiungiOnePieceSealedRequest;
import com.tsm.ur.card.seor.model.request.CancellaCartaOnePieceRequest;
import com.tsm.ur.card.seor.model.request.CancellaOnePieceSealedRequest;
import com.tsm.ur.card.seor.model.response.AggiungiCartaOnePieceResponse;
import com.tsm.ur.card.seor.model.response.AggiungiSealedOPResponse;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.OnePieceService;
import com.tsm.ur.card.seor.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/onepiece")
public class OnePieceController {

    private final OnePieceService onePieceService;
    private final JwtUtils jwtUtils;

    // ===================== SEALED APIs =====================

    @PostMapping("/add-sealed")
    public ResponseEntity<AggiungiSealedOPResponse> aggiungiSealedOP(
            @RequestBody AggiungiOnePieceSealedRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(onePieceService.aggiungiSealedOnePiece(username, request));
    }

    @PostMapping("/delete-sealed")
    public ResponseEntity<BaseResponse> cancellaSealedOP(
            @RequestBody CancellaOnePieceSealedRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(onePieceService.cancellaSealedOnePiece(username, request.idSealed()));
    }

    @GetMapping("/get-sealed/{idSealed}")
    public ResponseEntity<SealedOnePiece> getSealed(@PathVariable String idSealed) {
        return ResponseEntity.ok(onePieceService.getSealedOnePieceById(idSealed));
    }

    @GetMapping("/get-sealed-by-user")
    public ResponseEntity<List<SealedOnePiece>> getSealedByUser(Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(onePieceService.getSealedOnePieceByUsername(username));
    }

    @GetMapping("/get-sealed-bystato/{stato}")
    public ResponseEntity<List<SealedOnePiece>> getSealedByStato(
            @PathVariable String stato,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(onePieceService.getSealedOnePieceByUsernameAndStato(username, stato));
    }

    // ===================== CARD APIs =====================

    @PostMapping("/add-card")
    public ResponseEntity<AggiungiCartaOnePieceResponse> aggiungiCartaOP(
            @RequestBody AggiungiCartaOnePieceRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(onePieceService.aggiungiCartaOnePiece(username, request));
    }

    @PostMapping("/delete-card")
    public ResponseEntity<BaseResponse> cancellaCartaOP(
            @RequestBody CancellaCartaOnePieceRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(onePieceService.cancellaCartaOnePiece(username, request.idCarta()));
    }

    @GetMapping("/get-card/{idCarta}")
    public ResponseEntity<CartaOnePiece> getCard(@PathVariable String idCarta) {
        return ResponseEntity.ok(onePieceService.getCartaOnePieceById(idCarta));
    }

    @GetMapping("/get-cards-by-user")
    public ResponseEntity<List<CartaOnePiece>> getCardsByUser(Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(onePieceService.getCarteOnePieceByUsername(username));
    }

    @GetMapping("/get-cards-bystato/{stato}")
    public ResponseEntity<List<CartaOnePiece>> getCardsByStato(
            @PathVariable String stato,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(onePieceService.getCarteOnePieceByUsernameAndStato(username, stato));
    }
}

