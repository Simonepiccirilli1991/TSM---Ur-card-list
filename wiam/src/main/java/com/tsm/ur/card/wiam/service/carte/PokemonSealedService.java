package com.tsm.ur.card.wiam.service.carte;

import com.tsm.ur.card.wiam.entity.SealedPokemon;
import com.tsm.ur.card.wiam.exception.OnePieceException;
import com.tsm.ur.card.wiam.exception.PokemonException;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.AggiungiPokemonSealedRequest;
import com.tsm.ur.card.wiam.model.request.CancellaPokemonSealedRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiSealedPkmResponse;
import com.tsm.ur.card.wiam.repository.SealedPokemonRepo;
import com.tsm.ur.card.wiam.util.CarteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PokemonSealedService {


    private final SealedPokemonRepo sealedPokemonRepo;
    private final CarteUtil carteUtil;
    //TODO: agiungere servizi per add carte, cancella, get vari ecc. Implementare securety su orchestratore cache e fare FE con giro di test
    //TODO implemnetare le api di filtering e recap

    // salvo a db
    public AggiungiSealedPkmResponse aggiungiSealedPokm(AggiungiPokemonSealedRequest pokemonSealedRequest){
        log.info("AggiungiSealedPokemon service started with raw request: {}",pokemonSealedRequest);

        // valido request
        pokemonSealedRequest.validaRequest();
        // mappo su entity
        var entity = pokemonSealedRequest.requestToEntity();
        // setto id
        var id = carteUtil.generaIdSealedPokemon(entity.getUsernameAssociato());
        entity.setId(id);
        // salvo a db
        var sealed = sealedPokemonRepo.save(entity);
        log.info("AggiungiSealedPokemon service ended successfully with raw response: {}",sealed);
        return new AggiungiSealedPkmResponse("Aggiunto con successo",sealed);
    }

    // cancella sealed pokemon by id
    public BaseResponse cancellaSealedPokemonById(CancellaPokemonSealedRequest request){
        log.info("cancellaSealedPokemonById cancellaSealedPokemon started with raw request: {}",request);
        // controllo se sealed esiste
        var sealed = sealedPokemonRepo.findById(request.idSealed())
                .orElseThrow(() -> {
                    log.error("Sealed Pokemon con id: {} non trovato nel db",request.idSealed());
                    return new PokemonException("Sealed Pokemon con id: " + request.idSealed() + " non trovato","404","NOT_FOUND");
                });

        // controllo che userame combacino per effettuaare canellazione
        if(!sealed.getUsernameAssociato().equals(request.username())){
            log.error("Sealed Pokemon con id: {} non appartiene all'utente: {}",request.idSealed(),request.username());
            throw new PokemonException("Sealed Pokemon con id: " + request.idSealed() + " non appartiene all'utente: " + request.username(),
                    "403","FORBIDDEN");
        }

        sealedPokemonRepo.delete(sealed);
        log.info("Sealed Pokemon con id: {} cancellato correttamente dal db",sealed.getId());
        // preparo response
        var response = new BaseResponse("Sealed Pokemon cancellato correttamente",true);
        log.info("cancellaSealedPokemonById cancellaSealedPokemon ended successfully with response: {}",response);
        // ritorno response
        return response;
    }

    // get sealed by id
    public SealedPokemon getSealedPokemonById(String id){
        log.info("GetPokemonSealedByIdService getSealedPokemonById started with id: {}",id);
        var sealed = sealedPokemonRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Sealed One Piece con id: {} non trovato nel db",id);
                    return new PokemonException("Sealed Pokemon con id: " + id + " non trovato","404","NOT_FOUND");
                });
        log.info("GetPokemonSealedByIdService getSealedPokemonById ended successfully with sealed: {}",sealed);
        return sealed;
    }

    // ritorna tutto by username
    public List<SealedPokemon> getSealedPokemonByUsername(String username){
        log.info("GetPokemonSealedByUsernameService getSealedPokemonByUsername started with username: {}",username);
        var sealedList = sealedPokemonRepo.findByUsernameAssociato(username);
        log.info("GetPokemonSealedByUsernameService getSealedPokemonByUsername ended successfully with sealed list size: {}",sealedList.size());
        return sealedList;
    }

    // ritorna tutto by username e statoAcquisto acquistato & venduto
    public List<SealedPokemon> getSealedPokemonByUsernameAndStatoAcquisto(String username, String statoAcquisto){
        log.info("GetPokemonSealedByUsernameAndStatoService getSealedPokemonByUsernameAndStatoAcquisto started with username: {} and statoAcquisto: {}",username,statoAcquisto);
        var sealedList = sealedPokemonRepo.findByUsernameAssociatoAndStatoAcquisto(username, statoAcquisto);
        log.info("GetPokemonSealedByUsernameAndStatoService getSealedPokemonByUsernameAndStatoAcquisto ended successfully with sealed list size: {}",sealedList.size());
        return sealedList;
    }


}
