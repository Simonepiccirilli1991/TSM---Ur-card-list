package com.tsm.ur.card.authserver.controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tsm.ur.card.authserver.model.LoginRequest;
import com.tsm.ur.card.authserver.model.TokenResponse;
import com.tsm.ur.card.authserver.service.MongoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MongoUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWKSource<SecurityContext> jwkSource;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        try {
            log.info("Tentativo di login per username: {}", request.username());

            // Carica l'utente dal database
            var userDetails = userDetailsService.loadUserByUsername(request.username());

            // Verifica la password
            if (!passwordEncoder.matches(request.password(), userDetails.getPassword())) {
                log.warn("Password errata per username: {}", request.username());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new TokenResponse(null, null, "Credenziali non valide", 0L));
            }

            // Genera il JWT
            String accessToken = generateToken(userDetails.getUsername());

            log.info("Login riuscito per username: {}", request.username());

            return ResponseEntity.ok(new TokenResponse(
                    accessToken,
                    "Bearer",
                    "Login effettuato con successo",
                    3600L // 1 ora in secondi
            ));

        } catch (Exception e) {
            log.error("Errore durante il login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponse(null, null, "Credenziali non valide", 0L));
        }
    }

    private String generateToken(String username) throws Exception {
        // Crea un JWKSelector per selezionare chiavi RSA
        JWKMatcher jwkMatcher = new JWKMatcher.Builder()
                .keyType(com.nimbusds.jose.jwk.KeyType.RSA)
                .build();
        JWKSelector jwkSelector = new JWKSelector(jwkMatcher);

        // Ottieni la chiave RSA dal JWKSource
        var jwkList = jwkSource.get(jwkSelector, null);
        if (jwkList.isEmpty()) {
            throw new IllegalStateException("Nessuna chiave RSA trovata nel JWKSource");
        }
        RSAKey rsaKey = (RSAKey) jwkList.get(0);

        // Crea il signer
        JWSSigner signer = new RSASSASigner(rsaKey);

        // Ottieni l'utente completo per aggiungere claims
        var utente = userDetailsService.getUtenteByUsername(username);

        // Costruisci i claims del JWT
        Instant now = Instant.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("http://localhost:9001")  // Corretta porta auth server
                .audience("seor-client")
                .claim("username", username)
                .claim("email", utente != null ? utente.getEmail() : null)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .jwtID(UUID.randomUUID().toString())
                .build();

        // Crea e firma il JWT
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
                claimsSet
        );
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }
}

