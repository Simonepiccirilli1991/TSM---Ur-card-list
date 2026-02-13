package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.dto.SealedOnePiece;
import com.tsm.ur.card.seor.model.request.AggiungiOnePieceSealedRequest;
import com.tsm.ur.card.seor.model.request.CancellaOnePieceSealedRequest;
import com.tsm.ur.card.seor.model.response.AggiungiSealedOPResponse;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnePieceService {

    private final WiamIntegration wiamIntegration;

    public Mono<AggiungiSealedOPResponse> aggiungiSealedOnePiece(AggiungiOnePieceSealedRequest request) {
        log.info("OnePieceService - aggiungiSealedOnePiece per username: {}", request.username());
        return wiamIntegration.aggiungiSealedOnePiece(request);
    }

    public Mono<BaseResponse> cancellaSealedOnePiece(CancellaOnePieceSealedRequest request) {
        log.info("OnePieceService - cancellaSealedOnePiece per idSealed: {}", request.idSealed());
        return wiamIntegration.cancellaSealedOnePiece(request);
    }

    public Mono<SealedOnePiece> getSealedOnePieceById(String idSealed) {
        log.info("OnePieceService - getSealedOnePieceById per id: {}", idSealed);
        return wiamIntegration.getSealedOnePieceById(idSealed);
    }

    public Mono<List<SealedOnePiece>> getSealedOnePieceByUsername(String username) {
        log.info("OnePieceService - getSealedOnePieceByUsername per username: {}", username);
        return wiamIntegration.getSealedOnePieceByUsername(username);
    }

    public Mono<List<SealedOnePiece>> getSealedOnePieceByUsernameAndStato(String username, String stato) {
        log.info("OnePieceService - getSealedOnePieceByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamIntegration.getSealedOnePieceByUsernameAndStato(username, stato);
    }
}

