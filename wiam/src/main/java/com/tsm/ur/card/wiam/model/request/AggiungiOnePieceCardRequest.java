package com.tsm.ur.card.wiam.model.request;

import com.tsm.ur.card.wiam.entity.CartaOnePiece;
import com.tsm.ur.card.wiam.exception.OnePieceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.tsm.ur.card.wiam.util.WiamCostanti.StatiAcquisto.DISPONIBILE;
import static com.tsm.ur.card.wiam.util.WiamCostanti.Stato.ACQUISTATO;

@Slf4j
public record AggiungiOnePieceCardRequest(

        String usernameAssociato,
        String nome,
        String lingua,
        String espansione,
        Double prezzoAcquisto,
        LocalDateTime dataAcquisto,
        byte[] foto,

        // sezione gradata
        Boolean gradata,
        String enteGradazione,
        String votoGradazione,
        // sezione stato carta
        String statoCarta // mint, near mint, excellent, good, light played,
) {

    public void validaRequest(){
        if (ObjectUtils.isEmpty(usernameAssociato) || ObjectUtils.isEmpty(nome) || ObjectUtils.isEmpty(lingua)
        || ObjectUtils.isEmpty(espansione) || ObjectUtils.isEmpty(prezzoAcquisto) || ObjectUtils.isEmpty(dataAcquisto)
        || ObjectUtils.isEmpty(statoCarta)){
            log.error("AggiungiOnePieceCardRequest: Invalid request, parametri obbligatori mancanti");
            throw new OnePieceException("Errore nell'aggiunta one piece card, request invalida","Invalid request","WIAM-400op");
        }
    }

    public void validaRequestGradata(){
        if(Boolean.TRUE.equals(gradata)){
            if(ObjectUtils.isEmpty(enteGradazione) || ObjectUtils.isEmpty(votoGradazione)){
                log.error("AggiungiOnePieceCardRequest: Invalid request, parametri obbligatori mancanti per carta gradata");
                throw new OnePieceException("Errore nell'aggiunta one piece card gradata","Invalid request","WIAM-400op");
            }
        }
    }

    public CartaOnePiece mappaRequestToEntity(){
        var entity = new CartaOnePiece();
        entity.setUsernameAssociato(usernameAssociato);
        entity.setNome(nome);
        entity.setDataAcquisto(dataAcquisto.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        entity.setLingua(lingua);
        entity.setEspansione(espansione);
        entity.setPrezzoAcquisto(prezzoAcquisto);
        if(!ObjectUtils.isEmpty(foto))
            entity.setFoto(foto);
        if(!ObjectUtils.isEmpty(gradata))
            entity.setGradata(gradata);
        if(!ObjectUtils.isEmpty(enteGradazione))
            entity.setEnteGradazione(enteGradazione);
        if(!ObjectUtils.isEmpty(votoGradazione))
            entity.setVotoGradazione(votoGradazione);

        entity.setStatoCarta(statoCarta);
        // setto la parte statica
        entity.setStato(DISPONIBILE);
        entity.setStatoAcquisto(ACQUISTATO);
        return entity;
    }

}
