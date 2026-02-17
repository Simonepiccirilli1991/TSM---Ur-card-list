package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.request.RecuperoPswRequest;
import com.tsm.ur.card.seor.model.request.RegistraUtenteRequest;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtenteService {

    private final WiamIntegration wiamIntegration;

    public BaseResponse enrollUtente(String username) {
        log.info("UtenteService - enrollUtente per username: {}", username);
        return wiamIntegration.enrollUtente(username);
    }

    public BaseResponse cambioPassword(String username, String nuovaPassword) {
        log.info("UtenteService - cambioPassword per username: {}", username);
        return wiamIntegration.cambioPassword(username, nuovaPassword);
    }

    public BaseResponse recuperoPassword(RecuperoPswRequest request) {
        log.info("UtenteService - recuperoPassword per username: {}", request.username());
        return wiamIntegration.recuperoPassword(request);
    }

    public BaseResponse registraUtente(RegistraUtenteRequest request) {
        log.info("UtenteService - registraUtente per username: {}", request.username());
        return wiamIntegration.registraUtente(request);
    }
}

