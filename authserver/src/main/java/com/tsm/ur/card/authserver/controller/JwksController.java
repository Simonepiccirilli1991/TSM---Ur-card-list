package com.tsm.ur.card.authserver.controller;

import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwksController {

    private final JWKSource<SecurityContext> jwkSource;

    @GetMapping("/oauth2/jwks")
    public Map<String, Object> jwks() {
        try {
            // Crea un JWKSelector per selezionare tutte le chiavi
            JWKMatcher jwkMatcher = new JWKMatcher.Builder().build(); // Matcher vuoto = tutte le chiavi
            JWKSelector jwkSelector = new JWKSelector(jwkMatcher);

            var jwkList = jwkSource.get(jwkSelector, null);
            JWKSet jwkSet = new JWKSet(jwkList);
            return jwkSet.toJSONObject();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero delle chiavi JWKS", e);
        }
    }
}

