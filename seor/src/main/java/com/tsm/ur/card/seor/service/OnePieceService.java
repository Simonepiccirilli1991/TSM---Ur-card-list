package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.dto.CartaOnePiece;
import com.tsm.ur.card.seor.model.dto.SealedOnePiece;
import com.tsm.ur.card.seor.model.request.AggiungiCartaOnePieceRequest;
import com.tsm.ur.card.seor.model.request.AggiungiOnePieceSealedRequest;
import com.tsm.ur.card.seor.model.response.AggiungiCartaOnePieceResponse;
import com.tsm.ur.card.seor.model.response.AggiungiSealedOPResponse;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnePieceService {

    private final WiamIntegration wiamIntegration;

    // ===================== SEALED APIs =====================

    public AggiungiSealedOPResponse aggiungiSealedOnePiece(String username, AggiungiOnePieceSealedRequest request) {
        log.info("OnePieceService - aggiungiSealedOnePiece per username (JWT): {}", username);
        return wiamIntegration.aggiungiSealedOnePiece(username, request);
    }

    public BaseResponse cancellaSealedOnePiece(String username, String idSealed) {
        log.info("OnePieceService - cancellaSealedOnePiece per username (JWT): {}, idSealed: {}", username, idSealed);
        return wiamIntegration.cancellaSealedOnePiece(username, idSealed);
    }

    public SealedOnePiece getSealedOnePieceById(String idSealed) {
        log.info("OnePieceService - getSealedOnePieceById per id: {}", idSealed);
        return wiamIntegration.getSealedOnePieceById(idSealed);
    }

    public List<SealedOnePiece> getSealedOnePieceByUsername(String username) {
        log.info("OnePieceService - getSealedOnePieceByUsername per username: {}", username);
        return wiamIntegration.getSealedOnePieceByUsername(username);
    }

    public List<SealedOnePiece> getSealedOnePieceByUsernameAndStato(String username, String stato) {
        log.info("OnePieceService - getSealedOnePieceByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamIntegration.getSealedOnePieceByUsernameAndStato(username, stato);
    }

    // ===================== CARD APIs =====================

    public AggiungiCartaOnePieceResponse aggiungiCartaOnePiece(String username, AggiungiCartaOnePieceRequest request) {
        log.info("OnePieceService - aggiungiCartaOnePiece per username (JWT): {}", username);
        return wiamIntegration.aggiungiCartaOnePiece(username, request);
    }

    public BaseResponse cancellaCartaOnePiece(String username, String idCarta) {
        log.info("OnePieceService - cancellaCartaOnePiece per username (JWT): {}, idCarta: {}", username, idCarta);
        return wiamIntegration.cancellaCartaOnePiece(username, idCarta);
    }

    public CartaOnePiece getCartaOnePieceById(String idCarta) {
        log.info("OnePieceService - getCartaOnePieceById per id: {}", idCarta);
        return wiamIntegration.getCartaOnePieceById(idCarta);
    }

    public List<CartaOnePiece> getCarteOnePieceByUsername(String username) {
        log.info("OnePieceService - getCarteOnePieceByUsername per username: {}", username);
        return wiamIntegration.getCarteOnePieceByUsername(username);
    }

    public List<CartaOnePiece> getCarteOnePieceByUsernameAndStato(String username, String stato) {
        log.info("OnePieceService - getCarteOnePieceByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamIntegration.getCarteOnePieceByUsernameAndStato(username, stato);
    }
}

