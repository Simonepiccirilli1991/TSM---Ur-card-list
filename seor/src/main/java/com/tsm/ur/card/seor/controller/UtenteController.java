package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.request.CambioPswRequest;
import com.tsm.ur.card.seor.model.request.RecuperoPswRequest;
import com.tsm.ur.card.seor.model.request.RegistraUtenteRequest;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/utente")
public class UtenteController {

    private final UtenteService utenteService;

    @PostMapping("/enroll/{username}")
    public Mono<ResponseEntity<BaseResponse>> enrollUtente(@PathVariable String username) {
        return utenteService.enrollUtente(username)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/cambio-password")
    public Mono<ResponseEntity<BaseResponse>> cambioPasswordUtente(@RequestBody CambioPswRequest request) {
        return utenteService.cambioPassword(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/recupero-password")
    public Mono<ResponseEntity<BaseResponse>> recuperoPasswordUtente(@RequestBody RecuperoPswRequest request) {
        return utenteService.recuperoPassword(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/registra-utente")
    public Mono<ResponseEntity<BaseResponse>> registraUtente(@RequestBody RegistraUtenteRequest request) {
        return utenteService.registraUtente(request)
                .map(ResponseEntity::ok);
    }
}

