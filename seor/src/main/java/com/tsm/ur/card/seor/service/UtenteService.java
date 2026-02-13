package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.request.CambioPswRequest;
import com.tsm.ur.card.seor.model.request.RecuperoPswRequest;
import com.tsm.ur.card.seor.model.request.RegistraUtenteRequest;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtenteService {

    private final WiamIntegration wiamIntegration;

    public Mono<BaseResponse> enrollUtente(String username) {
        log.info("UtenteService - enrollUtente per username: {}", username);
        return wiamIntegration.enrollUtente(username);
    }

    public Mono<BaseResponse> cambioPassword(CambioPswRequest request) {
        log.info("UtenteService - cambioPassword per username: {}", request.username());
        return wiamIntegration.cambioPassword(request);
    }

    public Mono<BaseResponse> recuperoPassword(RecuperoPswRequest request) {
        log.info("UtenteService - recuperoPassword per username: {}", request.username());
        return wiamIntegration.recuperoPassword(request);
    }

    public Mono<BaseResponse> registraUtente(RegistraUtenteRequest request) {
        log.info("UtenteService - registraUtente per username: {}", request.username());
        return wiamIntegration.registraUtente(request);
    }
}

