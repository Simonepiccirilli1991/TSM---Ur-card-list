package com.tsm.ur.card.wiam.controller;

import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.CambioPswRequest;
import com.tsm.ur.card.wiam.model.request.RecuperoPswRequest;
import com.tsm.ur.card.wiam.model.request.RegistraUtenteRequest;
import com.tsm.ur.card.wiam.service.utenti.CambioPasswordService;
import com.tsm.ur.card.wiam.service.utenti.EnrollService;
import com.tsm.ur.card.wiam.service.utenti.RecuperoPswService;
import com.tsm.ur.card.wiam.service.utenti.RegistraUtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/utente")
public class UtenteController {

    private final EnrollService enrollService;
    private final CambioPasswordService cambioPasswordService;
    private final RecuperoPswService recuperoPswService;
    private final RegistraUtenteService registraUtenteService;


    @PostMapping("/enroll/{username}")
    public ResponseEntity<BaseResponse> enrollUtente(@PathVariable String username) {
        return ResponseEntity.ok(enrollService.enrollUtente(username));
    }


    @PostMapping("/cambio-password")
    public ResponseEntity<BaseResponse> cambioPasswordUtente(@RequestBody CambioPswRequest request) {
        return ResponseEntity.ok(cambioPasswordService.cambioPswd(request));
    }

    @PostMapping("/recupero-password")
    public ResponseEntity<BaseResponse> recuperoPasswordUtente(@RequestBody RecuperoPswRequest request) {
        return ResponseEntity.ok(recuperoPswService.recuperaPsw(request));
    }

    @PostMapping("/registra-utente")
    public ResponseEntity<BaseResponse> registraUtente(@RequestBody RegistraUtenteRequest username) {
        return ResponseEntity.ok(registraUtenteService.registraUtente(username));
    }
}
