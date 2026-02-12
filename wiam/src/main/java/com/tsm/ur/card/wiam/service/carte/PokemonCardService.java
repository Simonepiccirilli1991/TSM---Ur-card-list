package com.tsm.ur.card.wiam.service.carte;

import com.tsm.ur.card.wiam.entity.CartaPokemon;
import com.tsm.ur.card.wiam.exception.PokemonException;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.AggiungiCartaPokemonRequest;
import com.tsm.ur.card.wiam.model.request.CancellaCartaPokemonRequest;
import com.tsm.ur.card.wiam.model.response.AggiungiCartaPokemonResponse;
import com.tsm.ur.card.wiam.repository.CardPokemonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PokemonCardService {


    private final CardPokemonRepo cardPokemonRepo;


    public AggiungiCartaPokemonResponse aggiungiCartaPokemonResponse(AggiungiCartaPokemonRequest request){
        log.info("AggiungiCartaPokemon Service started with raw request: {}",request);
        // valido request
        request.validaRequest();
        // controllo se carta gradata e valido
        request.validaRequestGradata();
        // mappo request su entity
        var entity = request.mappaRequestToEntity();

        var carta = cardPokemonRepo.save(entity);
        log.info("AggiungiCartaPokemon Service ended successfully with response: {}",entity);
        return new AggiungiCartaPokemonResponse("Carta pokemon salvata con successo", carta);
    }


    public BaseResponse cancellaCartaPokemonById(CancellaCartaPokemonRequest request){
        log.info("CancellaCartaPokemon Service started with raw request: {}",request);
        // controllo se carta esiste
        var carta = cardPokemonRepo.findById(request.idCarta())
                .orElseThrow(() -> {
                    log.error("Carta Pokemon con id: {} non trovata nel db",request.idCarta());
                    return new PokemonException("Carta Pokemon con id: " + request.idCarta() + " non trovata","404","NOT_FOUND");
                });

        // controllo che la persona che richiede la cancellazione sia l'associato alla carta
        if(!carta.getUsernameAssociato().equals(request.username())){
            log.error("Utente: {} non autorizzato a cancellare carta con id: {}",request.username(),request.idCarta());
            throw new PokemonException("Utente: " + request.username() + " non autorizzato a cancellare carta con id: " + request.idCarta(),"403","FORBIDDEN");
        }
        // cancello carta
        cardPokemonRepo.delete(carta);
        log.info("CancellaCartaPokemon Service ended successfully for carta with id: {}",request.idCarta());
        return new BaseResponse("Carta pokemon cancellata con successo",true);
    }

    public CartaPokemon getCartaPokemonById(String idCarta){
        log.info("GetCartaPokemonById Service started with idCarta: {}",idCarta);
        var carta = cardPokemonRepo.findById(idCarta)
                .orElseThrow(() -> {
                    log.error("Carta Pokemon con id: {} non trovata nel db",idCarta);
                    return new PokemonException("Carta Pokemon con id: " + idCarta + " non trovata","404","NOT_FOUND");
                });
        log.info("GetCartaPokemonById Service ended successfully with carta: {}",carta);
        return carta;
    }

    public List<CartaPokemon> getCartePokemonByUsername(String username){
        log.info("GetCartePokemonByUsername Service started with username: {}",username);
        var carte = cardPokemonRepo.findByUsernameAssociato(username);
        log.info("GetCartePokemonByUsername Service ended successfully with carte: {}",carte);
        return carte;
     }

     public List<CartaPokemon> getCartePokemonByUsernameAndStatoAcquisto(String username, String statoAcquisto){
        log.info("GetCartePokemonByUsernameAndGradata Service started with username: {} and statoAcquisto: {}",username,statoAcquisto);
        var carte = cardPokemonRepo.findByUsernameAssociatoAndStatoAcquisto(username,statoAcquisto);
        log.info("GetCartePokemonByUsernameAndGradata Service ended successfully with carte: {}",carte);
        return carte;
     }
}
