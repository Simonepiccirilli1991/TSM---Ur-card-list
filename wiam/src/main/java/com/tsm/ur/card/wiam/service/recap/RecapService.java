package com.tsm.ur.card.wiam.service.recap;


import com.tsm.ur.card.wiam.model.BaseRecap;
import com.tsm.ur.card.wiam.model.request.RecapRequest;
import com.tsm.ur.card.wiam.service.MapperService;
import com.tsm.ur.card.wiam.service.carte.OpCardService;
import com.tsm.ur.card.wiam.service.carte.OpSealedService;
import com.tsm.ur.card.wiam.service.carte.PokemonCardService;
import com.tsm.ur.card.wiam.service.carte.PokemonSealedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class RecapService {

    private final OpCardService opCardService;
    private final OpSealedService opSealedService;
    private final PokemonCardService pokemonCardService;
    private final PokemonSealedService pokemonSealedService;
    private final MapperService mapperService;



    public List<BaseRecap> getRecap(RecapRequest recapRequest){
        log.info("RecapService start with raw request: {}",recapRequest);


        var listaRecap = new ArrayList<BaseRecap>();
        //devo recuperare i dati in base al caso d'uso
       switch (recapRequest){

            case RecapRequest i when "ONEPIECE".equals(i.tipoProdotto()) -> {
                log.info("RecapService ONEPIECE case");
                getRecapOnePiece(recapRequest,listaRecap);
            }

            case RecapRequest i when "POKEMON".equals(i.tipoProdotto()) -> {
                log.info("RecapService POKEMON case");
                getRecapPokemon(recapRequest,listaRecap);
            }


            default -> {
                log.info("RecapService default case");
                getRecapOnePiece(recapRequest,listaRecap);
                getRecapPokemon(recapRequest,listaRecap);
            }
        };

        log.info("RecapService end successfully with recap list of size: {}", listaRecap.size());
        return listaRecap;
    }


    private void getRecapOnePiece(RecapRequest recapRequest, List<BaseRecap> listaRecap){
        //recupero carte one piece
        // controllo se è recap carte con stato
        if(!ObjectUtils.isEmpty(recapRequest.stato())){
            log.info("Recupero recap carte one piece con stato: {}",recapRequest.stato());
            var recapOpCard = opCardService.getCartaOpByStatoAndUsername(recapRequest.stato(), recapRequest.username());
            // recupero recap sealed one piece con stato
            log.info("Recupero recap sealed one piece con stato: {}",recapRequest.stato());
            var recapOpSealed = opSealedService.getSealedOnePieceByUsernameAndStatoAcquisto(recapRequest.username(), recapRequest.stato());
            // mappo le 2 liste su dto generico

            // ciclo le lista carte e mappo su dto generico
            recapOpCard.forEach(i -> {
                var dto = (BaseRecap) mapperService.mappaDtoSuRecap(i, BaseRecap.class);
                // setto tipo e tipo prodotto
                dto.setTipoProdotto("ONEPIECE");
                dto.setTipo("CARD");
                listaRecap.add(dto);
            });
            // ciclo lista sealed e mappo su dto generico
            recapOpSealed.forEach(i -> {
                var dto = (BaseRecap) mapperService.mappaDtoSuRecap(i, BaseRecap.class);
                dto.setTipoProdotto("ONEPIECE");
                dto.setTipo("SEALED");
                listaRecap.add(dto);
            });
        }
        // caso d'uso recap carte senza stato
        else {
            log.info("Recupero recap carte one piece senza stato");
            var recapOpCard = opCardService.getCartaOPByUsername(recapRequest.username());
            var recapOpSealed = opSealedService.getSealedOnePieceByUsername(recapRequest.username());
            // ciclo liste e addo
            recapOpCard.forEach(i -> {
                var dto = (BaseRecap) mapperService.mappaDtoSuRecap(i, BaseRecap.class);
                dto.setTipoProdotto("ONEPIECE");
                dto.setTipo("CARD");
                listaRecap.add(dto);
            });
            // ciclo lista sealed e mappo su dto generico
            recapOpSealed.forEach(i -> {
                var dto = (BaseRecap) mapperService.mappaDtoSuRecap(i, BaseRecap.class);
                dto.setTipoProdotto("ONEPIECE");
                dto.setTipo("SEALED");
                listaRecap.add(dto);
            });
        }
    }

    private void getRecapPokemon(RecapRequest recapRequest, List<BaseRecap> listaRecap) {
        //recupero carte pokemon
        // controllo se è recap carte con stato
        if (!ObjectUtils.isEmpty(recapRequest.stato())) {
            log.info("Recupero recap carte pokemon con stato: {}", recapRequest.stato());
            var recapPkmCard = pokemonCardService.getCartePokemonByUsernameAndStatoAcquisto(recapRequest.username(), recapRequest.stato());
            // recupero recap sealed pokemon con stato
            log.info("Recupero recap sealed pokemon con stato: {}", recapRequest.stato());
            var recapPkmSealed = pokemonSealedService.getSealedPokemonByUsernameAndStatoAcquisto(recapRequest.username(), recapRequest.stato());
            // mappo le 2 liste su dto generico

            // ciclo le lista carte e mappo su dto generico
            recapPkmCard.forEach(i -> {
                var dto = (BaseRecap) mapperService.mappaDtoSuRecap(i, BaseRecap.class);
                // setto tipo e tipo prodotto
                dto.setTipoProdotto("POKEMON");
                dto.setTipo("CARD");
                listaRecap.add(dto);
            });
            // ciclo lista sealed e mappo su dto generico
            recapPkmSealed.forEach(i -> {
                var dto = (BaseRecap) mapperService.mappaDtoSuRecap(i, BaseRecap.class);
                dto.setTipoProdotto("POKEMON");
                dto.setTipo("SEALED");
                listaRecap.add(dto);
            });
        }
        // caso d'uso recap carte senza stato
        else {
            log.info("Recupero recap carte pokemon senza stato");
            var recapPkmCard = pokemonCardService.getCartePokemonByUsername(recapRequest.username());
            var recapPkmSealed = pokemonSealedService.getSealedPokemonByUsername(recapRequest.username());
            // ciclo liste e addo
            recapPkmCard.forEach(i -> {
                var dto = (BaseRecap) mapperService.mappaDtoSuRecap(i, BaseRecap.class);
                dto.setTipoProdotto("POKEMON");
                dto.setTipo("CARD");
                listaRecap.add(dto);
            });
            // ciclo lista sealed e mappo su dto generico
            recapPkmSealed.forEach(i -> {
                var dto = (BaseRecap) mapperService.mappaDtoSuRecap(i, BaseRecap.class);
                dto.setTipoProdotto("POKEMON");
                dto.setTipo("SEALED");
                listaRecap.add(dto);
            });
        }
    }

}
