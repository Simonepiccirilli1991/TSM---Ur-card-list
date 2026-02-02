package com.tsm.ur.card.wiam.service.carte;

import com.tsm.ur.card.wiam.model.request.AggiungiOnePiceSealedRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiSealedOPResponse;
import com.tsm.ur.card.wiam.repository.SealedOnePieceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AggiungiOpSealedService {

    //TODO: agiungere servizi per add carte, cancella, get vari ecc. Implementare securety su orchestratore cache e fare FE con giro di test
    //TODO implemnetare le api di filtering e recap

    private final SealedOnePieceRepo sealedOnePieceRepo;

    public AggiungiSealedOPResponse aggiungiSealedOnePiece(AggiungiOnePiceSealedRequest request){
        log.info("AggiungiOpSealedService started with raw request: {}",request);

        // valido request
        request.validaRequest();
        // mappo su entity
        var entity = request.requestToEntity();
        // salvo su db
        var sealed = sealedOnePieceRepo.save(entity);
        log.info("Sealed One Piece salvato correttamente nel db con id: {}",sealed.getId());
        // preparo response
        var response = new AggiungiSealedOPResponse(
                "Sealed One Piece aggiunto correttamente",
                sealed
        );
        log.info("AggiungiOpSealedService ended successfully with response: {}",response);
        // ritorno response
        return response;
    }
}
