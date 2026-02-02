package com.tsm.ur.card.wiam.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CarteUtil {


    public String generaIdCartaPokemon(String username){

        var idCarta = "PKM-CARD-" + System.currentTimeMillis() + "-" + username;
        log.info("Generated OnePiece card ID: {}", idCarta);
        return idCarta;
    }

    public String generaIdSealedPokemon(String username){

        var idSealed = "PKM-SEALED-" + System.currentTimeMillis() + "-" + username;
        log.info("Generated OnePiece sealed ID: {}", idSealed);
        return idSealed;
    }

    public String generaIdCartaOnePiece(String username){

        var idCarta = "OP-CARD-" + System.currentTimeMillis() + "-" + username;
        log.info("Generated OnePiece card ID: {}", idCarta);
        return idCarta;
    }

    public String generaIdSealedOnePiece(String username){
        var idSealed = "OP-SEALED-" + System.currentTimeMillis() + "-" + username;
        log.info("Generated OnePiece sealed ID: {}", idSealed);
        return idSealed;
    }
}
