package com.tsm.ur.card.wiam.service.carte;

import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import com.tsm.ur.card.wiam.exception.OnePieceException;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.AggiungiOnePiceSealedRequest;
import com.tsm.ur.card.wiam.model.request.CancellaOpSealedRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiSealedOPResponse;
import com.tsm.ur.card.wiam.repository.SealedOnePieceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpSealedService {

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


    // cancella sealed per id
    public BaseResponse cancellaSealedOnePiece(CancellaOpSealedRequest request){
        log.info("AggiungiOpSealedService cancellaSealedOnePiece started with raw request: {}",request);
        // controllo se sealed esiste
        var sealed = sealedOnePieceRepo.findById(request.idSealed())
                        .orElseThrow(() -> {
                            log.error("Sealed One Piece con id: {} non trovato nel db",request.idSealed());
                            return new OnePieceException("Sealed One Piece con id: " + request.idSealed() + " non trovato","404","NOT_FOUND");
                        });

        // controllo che userame combacino per effettuaare canellazione
        if(!sealed.getUsernameAssociato().equals(request.username())){
            log.error("Sealed One Piece con id: {} non appartiene all'utente: {}",request.idSealed(),request.username());
            throw new OnePieceException("Sealed One Piece con id: " + request.idSealed() + " non appartiene all'utente: " + request.username(),
                    "403","FORBIDDEN");
        }

        sealedOnePieceRepo.delete(sealed);
        log.info("Sealed One Piece con id: {} cancellato correttamente dal db",sealed.getId());
        // preparo response
        var response = new BaseResponse("Sealed One Piece cancellato correttamente",true);
        log.info("AggiungiOpSealedService cancellaSealedOnePiece ended successfully with response: {}",response);
        // ritorno response
        return response;
    }

    // get sealed by id
    public SealedOnePiece getSealedOnePieceById(String id){
        log.info("GetOpSealedByIdService getSealedOnePieceById started with id: {}",id);
        var sealed = sealedOnePieceRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Sealed One Piece con id: {} non trovato nel db",id);
                    return new OnePieceException("Sealed One Piece con id: " + id + " non trovato","404","NOT_FOUND");
                });
        log.info("GetOpSealedByIdService getSealedOnePieceById ended successfully with sealed: {}",sealed);
        return sealed;
    }

    // ritorna tutto by username
    public List<SealedOnePiece> getSealedOnePieceByUsername(String username){
        log.info("GetOpSealedByUsernameService getSealedOnePieceByUsername started with username: {}",username);
        var sealedList = sealedOnePieceRepo.findByUsernameAssociato(username);
        log.info("GetOpSealedByUsernameService getSealedOnePieceByUsername ended successfully with sealed list size: {}",sealedList.size());
        return sealedList;
    }

    // ritorna tutto by username e statoAcquisto acquistato & venduto
    public List<SealedOnePiece> getSealedOnePieceByUsernameAndStatoAcquisto(String username, String statoAcquisto){
        log.info("GetOpSealedByUsernameAndStatoService getSealedOnePieceByUsernameAndStatoAcquisto started with username: {} and statoAcquisto: {}",username,statoAcquisto);
        var sealedList = sealedOnePieceRepo.findByUsernameAssociatoAndStatoAcquisto(username, statoAcquisto);
        log.info("GetOpSealedByUsernameAndStatoService getSealedOnePieceByUsernameAndStatoAcquisto ended successfully with sealed list size: {}",sealedList.size());
        return sealedList;
    }
}
