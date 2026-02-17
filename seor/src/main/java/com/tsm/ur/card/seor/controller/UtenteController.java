package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.request.CambioPswRequest;
import com.tsm.ur.card.seor.model.request.RecuperoPswRequest;
import com.tsm.ur.card.seor.model.request.RegistraUtenteRequest;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.UtenteService;
import com.tsm.ur.card.seor.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/utente")
public class UtenteController {

    private final UtenteService utenteService;
    private final JwtUtils jwtUtils;

    @PostMapping("/enroll")
    public ResponseEntity<BaseResponse> enrollUtente(Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(utenteService.enrollUtente(username));
    }

    @PostMapping("/cambio-password")
    public ResponseEntity<BaseResponse> cambioPasswordUtente(
            @RequestBody CambioPswRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(utenteService.cambioPassword(username, request.nuovaPassword()));
    }

    // API PUBBLICA - Non richiede JWT
    @PostMapping("/recupero-password")
    public ResponseEntity<BaseResponse> recuperoPasswordUtente(@RequestBody RecuperoPswRequest request) {
        return ResponseEntity.ok(utenteService.recuperoPassword(request));
    }

    // API PUBBLICA - Non richiede JWT
    @PostMapping("/registra-utente")
    public ResponseEntity<BaseResponse> registraUtente(@RequestBody RegistraUtenteRequest request) {
        return ResponseEntity.ok(utenteService.registraUtente(request));
    }
}

