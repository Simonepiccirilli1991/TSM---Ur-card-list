package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.response.BaseRecap;
import com.tsm.ur.card.seor.service.RecapService;
import com.tsm.ur.card.seor.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recap")
public class RecapController {

    private final RecapService recapService;
    private final JwtUtils jwtUtils;

    @GetMapping("/getrecap")
    public ResponseEntity<List<BaseRecap>> getRecap(Authentication authentication) {
        String username = jwtUtils.extractUsername(authentication);
        return ResponseEntity.ok(recapService.getRecap(username));
    }
}

