package com.tsm.ur.card.wiam.service.utenti;

import com.tsm.ur.card.wiam.exception.UtenteException;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.CambioPswRequest;
import com.tsm.ur.card.wiam.repository.UtenteRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CambioPasswordService {


    private final UtenteRepo utenteRepo;


    public BaseResponse cambioPswd(CambioPswRequest request) {
        log.info("CambioPswService started with raw request: {}",request);

        // retrivo utente
        var utente = utenteRepo.findByUsername(request.username())
                .orElseThrow(() -> {
                log.error("Errore in cambioPsw utente non trovato per username : {}",request.username());
                return new UtenteException("Utente non trovato","WIAM-404x", "Utente non registrato a DB");
                });

        // controllo se psw nuova e vecchai sono uguali in caso blocco
        if(utente.getPassword().equals(request.nuovaPassword())){
            log.error("Errore in cambioPsw password nuova uguale a quella vecchia per username : {}",request.username());
            throw new UtenteException("La password nuova non può essere uguale a quella vecchia","WIAM-400x", "Password nuova identica a quella vecchia");
        }

        // aggiorno psw
        utente.setPassword(request.nuovaPassword());
        utenteRepo.save(utente);
        log.info("Password cambiata con successo per username: {}",request.username());
        return new BaseResponse("Password cambiata con successo", true);
    }
}
