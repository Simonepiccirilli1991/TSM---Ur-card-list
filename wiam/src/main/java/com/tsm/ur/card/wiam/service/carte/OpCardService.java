package com.tsm.ur.card.wiam.service.carte;


import com.tsm.ur.card.wiam.entity.CartaOnePiece;
import com.tsm.ur.card.wiam.except.OnePieceException;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.AggiungiOnePieceCardRequest;
import com.tsm.ur.card.wiam.model.request.CancellaCartaOpRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiCartaOPResponse;
import com.tsm.ur.card.wiam.repository.CardOnePieceRepo;
import com.tsm.ur.card.wiam.util.CarteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public BaseResponse cancellaCartaOP(CancellaCartaOpRequest request){
        log.info("CAncellaCartaOP Service started with raw request:: {}",request);
        // controllo se carta esiste
        var carta = cardOnePieceRepo.findById(request.idCarta())
                .orElseThrow(() -> {
                    log.error("Carta One Piece con id: {} non trovata nel db",request.idCarta());
                    return new RuntimeException("Carta One Piece con id: " + request.idCarta() + " non trovata");
                });

        // controllo che la persona che richiede la cancellazione sia l'associato alla carta
        if(!carta.getUsernameAssociato().equals(request.username())){
            log.error("Utente: {} non autorizzato a cancellare carta con id: {}",request.username(),request.idCarta());
            throw new OnePieceException("Utente: " + request.username() + " non autorizzato a cancellare carta con id: " + request.idCarta(),"403","FORBIDDEN");
        }
        // cancello carta
        cardOnePieceRepo.delete(carta);
        log.info("Carta One Piece con id: {} cancellata correttamente",request.idCarta());
        return new BaseResponse("Carta One Piece con id: " + request.idCarta() + " cancellata correttamente",true);
    }


    public CartaOnePiece getCartaOPById(String id){
        log.info("getCartaOPById started with id: {}",id);
        var carta = cardOnePieceRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Carta One Piece con id: {} non trovata nel db",id);
                    return new RuntimeException("Carta One Piece con id: " + id + " non trovata");
                });
        log.info("getCartaOPById ended successfully with carta: {}",carta);
        return carta;
    }

    public List<CartaOnePiece> getCartaOPByUsername(String username){
        log.info("getCartaOPByUsername started with username: {}",username);
        var carte = cardOnePieceRepo.findByUsername(username);
        log.info("getCartaOPByUsername ended successfully with carte: {}",carte);
        return carte;
    }

    public List<CartaOnePiece>getCartaOpByStatoAndUsername(String stato, String username) {
        log.info("getCartaOpByStatoAndUsername started with stato: {} and username: {}", stato, username);
        var carte = cardOnePieceRepo.findByUsernameAssociatoAndStatoAcquisto(username, stato);
        log.info("getCartaOpByStatoAndUsername ended successfully with carte: {}", carte);
        return carte;

    }

}
