package com.tsm.ur.card.wiam.service.utenti;

import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.RecuperoPswRequest;
import com.tsm.ur.card.wiam.repository.UtenteRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecuperoPswService {


    private final UtenteRepo utenteRepo;

    // aggiorno utenza con nuvoa psw, in quanto se arriva qui ha superato check otp
    public BaseResponse recuperaPsw(RecuperoPswRequest request){
        log.info("RecuperoPswService started with raw request: {}",request);

        var utente = utenteRepo.findByUsername(request.username())
                .orElseThrow(() -> {
                    log.error("Utente {} non trovato durante il recupero password",request.username());
                    return new RuntimeException("Utente non trovato");
                });

        // aggiorno psw
        utente.setPassword(request.nuovaPassword());
        utenteRepo.save(utente);

        log.info("RecuperoPswService completed for user: {}",request.username());
        return new BaseResponse("Password aggiornata con successo per utente "+request.username(),true);
    }
}
