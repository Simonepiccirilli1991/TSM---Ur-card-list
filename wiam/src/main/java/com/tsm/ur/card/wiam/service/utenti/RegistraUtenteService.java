package com.tsm.ur.card.wiam.service.utenti;


import com.tsm.ur.card.wiam.entity.Utente;
import com.tsm.ur.card.wiam.except.UtenteException;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.RegistraUtenteRequest;
import com.tsm.ur.card.wiam.repository.UtenteRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistraUtenteService {

    private final UtenteRepo utenteRepo;


    public BaseResponse registraUtente(RegistraUtenteRequest registraUtenteRequest) {
        log.info("RegistraUtenteService started with raw request: {}",registraUtenteRequest);

        // salvo utente
        // controllo che l'utente non esista già
        var existingUtente = utenteRepo.findByUsername(registraUtenteRequest.username());
        if(existingUtente.isPresent()){
            log.info("Utente già esistente con username: {}",registraUtenteRequest.username());
            return new BaseResponse("Utente già esistente", false);
        }
        // controllo che email non sia gia registrata altrimenti eccezzione
        var existingEmail = utenteRepo.findByEmail(registraUtenteRequest.email());
        if(existingEmail.isPresent()){
            log.info("Email già registrata: {}",registraUtenteRequest.email());
            throw new UtenteException("Email già registrata","WIAM-400x","Email gia registrata");
        }
        var entity = new Utente();
        entity.setEmail(registraUtenteRequest.email());
        entity.setPassword(registraUtenteRequest.password());
        entity.setUsername(registraUtenteRequest.username());
        entity.setDataRegistrazione(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        var utente = utenteRepo.save(entity);

        log.info("Utente registrato con successo con username: {}",utente.getUsername());

        return new BaseResponse("Utente registrato con successo", true);
    }
}
