package com.tsm.ur.card.wiam.controller;

import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.VendiProdottoRequest;
import com.tsm.ur.card.wiam.service.vendita.VenditaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vendita")
public class VenditaController {

    private final VenditaService venditaService;

    @PostMapping("/vendi")
    public ResponseEntity<BaseResponse> vendiProdotto(@RequestBody VendiProdottoRequest request) {
        return ResponseEntity.ok(venditaService.vendiProdotto(request));
    }
}

