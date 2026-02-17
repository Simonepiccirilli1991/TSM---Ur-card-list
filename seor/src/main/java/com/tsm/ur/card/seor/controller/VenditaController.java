package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.request.VendiProdottoRequest;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.VenditaService;
import com.tsm.ur.card.seor.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vendita")
public class VenditaController {

    private final VenditaService venditaService;
    private final JwtUtils jwtUtils;

    @PostMapping("/vendi")
    public ResponseEntity<BaseResponse> vendiProdotto(
            @RequestBody VendiProdottoRequest request,
            Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(venditaService.vendiProdotto(username, request));
    }
}

