package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.request.CambioPswRequest;
import com.tsm.ur.card.seor.model.request.RecuperoPswRequest;
import com.tsm.ur.card.seor.model.request.RegistraUtenteRequest;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/utente")
public class UtenteController {

    private final UtenteService utenteService;

    @PostMapping("/enroll/{username}")
    public ResponseEntity<BaseResponse> enrollUtente(@PathVariable String username) {
        return  ResponseEntity.ok(utenteService.enrollUtente(username));
    }

    @PostMapping("/cambio-password")
    public ResponseEntity<BaseResponse> cambioPasswordUtente(@RequestBody CambioPswRequest request) {
        return  ResponseEntity.ok(utenteService.cambioPassword(request));
    }

    @PostMapping("/recupero-password")
    public ResponseEntity<BaseResponse> recuperoPasswordUtente(@RequestBody RecuperoPswRequest request) {
        return  ResponseEntity.ok(utenteService.recuperoPassword(request));
    }

    @PostMapping("/registra-utente")
    public ResponseEntity<BaseResponse> registraUtente(@RequestBody RegistraUtenteRequest request) {
        return  ResponseEntity.ok(utenteService.registraUtente(request));
    }
}

