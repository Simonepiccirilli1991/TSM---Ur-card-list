package com.tsm.ur.card.wiam.service.carte;


import com.tsm.ur.card.wiam.model.request.AggiungiOnePieceCardRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiCartaOPResponse;
import com.tsm.ur.card.wiam.repository.CardOnePieceRepo;
import com.tsm.ur.card.wiam.util.CarteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpCardService {


    private final CardOnePieceRepo cardOnePieceRepo;
    private final CarteUtil carteUtil;



    public AggiungiCartaOPResponse aggiungiCartaOP(AggiungiOnePieceCardRequest request) {
        log.info("AggiungiCartaOp Service started with raw request: {}",request);
        // valido
        request.validaRequest();
        // valido in caso di gradata
        request.validaRequestGradata();
        // mappo request su entity
        var entity = request.mappaRequestToEntity();
        var id = carteUtil.generaIdCartaOnePiece(entity.getUsernameAssociato());
        entity.setId(id);
        // salvo a a db
        var carta = cardOnePieceRepo.save(entity);
        log.info("AggiungiCartaOp Service ended successfully with response: {}",entity);
        return new AggiungiCartaOPResponse("Carta one piece salvata con successo", carta);
    }

    //TODO: FARE IMPLEMENTAZIONE CARTE E POI I RECAP , SESSIONE SICUREZZA CON CACHE E LOGIN E POI FE
}
