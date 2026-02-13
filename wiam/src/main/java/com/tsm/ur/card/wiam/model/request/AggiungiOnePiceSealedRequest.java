package com.tsm.ur.card.wiam.model.request;

import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import com.tsm.ur.card.wiam.except.OnePieceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.tsm.ur.card.wiam.util.WiamCostanti.StatiAcquisto.DISPONIBILE;
import static com.tsm.ur.card.wiam.util.WiamCostanti.Stato.ACQUISTATO;

@Slf4j
public record AggiungiOnePiceSealedRequest
        (String username,
         String nome,
         String linguea,
         String espansione,
         Double prezzoAcquisto,
         LocalDateTime dataAcquisto,
         LocalDateTime dataUscitaProdottoUfficiale,
         byte[] foto,
         String acquistatoPresso){


        public void validaRequest(){
            if(ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(nome) ||
            ObjectUtils.isEmpty(linguea) || ObjectUtils.isEmpty(prezzoAcquisto) ||
            ObjectUtils.isEmpty(dataAcquisto)){
                log.error("Errore di validazione della request: {}",this);
                throw new OnePieceException("Campi obbligatori mancanti nella request","Invalid REquest","WIAM-400op");
            }
        }


        public SealedOnePiece requestToEntity(){

            var entity = new SealedOnePiece();
            entity.setUsernameAssociato(this.username);
            entity.setNome(this.nome);
            entity.setLingua(this.linguea);
            if(ObjectUtils.isEmpty(this.espansione))
                entity.setEspansione(this.espansione);

            entity.setPrezzoAcquisto(this.prezzoAcquisto);
            entity.setDataAcquisto(this.dataAcquisto.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            if(!ObjectUtils.isEmpty(this.dataUscitaProdottoUfficiale))
                entity.setDataUscitaProdottoUfficiale(this.dataUscitaProdottoUfficiale.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            if (!ObjectUtils.isEmpty(this.foto))
                entity.setFoto(this.foto);
            if(!ObjectUtils.isEmpty(this.acquistatoPresso))
                entity.setAcquistatoPresso(this.acquistatoPresso);

            entity.setStato(DISPONIBILE);
            entity.setStatoAcquisto(ACQUISTATO);
            return entity;
        }
}
