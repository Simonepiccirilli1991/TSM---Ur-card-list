package com.tsm.ur.card.wiam.service.utenti;

import com.tsm.ur.card.wiam.except.UtenteException;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.repository.UtenteRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollService {

    private final UtenteRepo utenteRepo;

    // metodo per enroll utente
    public BaseResponse enrollUtente(String username){
        log.info("EnrollService started for user: {}",username);

        // qui andrebbe la logica di enroll dell'utente
        // per ora fittizia
        var utente = utenteRepo.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Utente {} non trovato durante l'enroll",username);
                    return new UtenteException("Errore durante l'enroll","WIAM-404x", "Utente non trovato");
                });

        // se a  true l'enroll
        utente.setEnrollment(true);
        log.info("EnrollService completed for user: {}",username);
        return new BaseResponse("Utente "+username+" enrollato con successo",true);
    }

    // metodo per aumentare e controllare OTP per enroll
    public BaseResponse aumentaContatoreOtpEnroll(String username){
        log.info("AumentaContatoreOtpEnroll started for user: {}",username);

        var utente = utenteRepo.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Utente {} non trovato durante l'aumento del contatore OTP enroll",username);
                    return new UtenteException("Errore durante l'aumento del contatore OTP enroll","WIAM-404x", "Utente non trovato");
                });

        // controllo se il contatore enroll e gia al massimo di 5 per la giornata di oggi, se cosi blocco
        if(utente.getOtpCount() >= 5 && Boolean.FALSE.equals(calcolaOtpEnrollGiornaliero(utente.getOtpLastTime()))){
            log.error("Utente {} ha raggiunto il limite massimo di OTP enroll per oggi",username);
            throw new UtenteException("Limite massimo di OTP enroll raggiunto per oggi","WIAM-400x", "Limite OTP enroll raggiunto");
        }

        // se sono arrivato qui e il counter e gia a 5 lo resetto a 1
        if(5 == utente.getOtpCount()){
            log.info("Reset contatore OTP enroll per utente: {}",username);
            utente.setOtpCount(1);
        }
        // aumento contatore otp
        else{
            log.info("Aumento contatore OTP enroll per utente: {}",username);
            utente.setOtpCount(utente.getOtpCount()+1);
        }
        // aggiorno data ultimo otp
        utente.setOtpLastTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("AumentaContatoreOtpEnroll completed for user: {}",username);
        return new BaseResponse("Contatore OTP enroll aumentato per utente "+username, true);
    }


    private boolean calcolaOtpEnrollGiornaliero(String utiloOtpData){

        var dataUltimoOtp = LocalDateTime.parse(utiloOtpData);
        // se la data dell'ultimo otp +1 giorno e prima di adesso ritorno true perche e passato un giorno e puo richiedere otp, altrimenti ritorno false perche non e passato un giorno e non puo richiedere otp
        if(dataUltimoOtp.plusDays(1).isBefore(LocalDateTime.now()))
            return true;
        // puo richiedere otp
        else
            return false;


    }
}
