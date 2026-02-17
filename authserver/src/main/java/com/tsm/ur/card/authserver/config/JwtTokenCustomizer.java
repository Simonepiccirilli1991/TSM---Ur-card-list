package com.tsm.ur.card.authserver.config;

import com.tsm.ur.card.authserver.entity.Utente;
import com.tsm.ur.card.authserver.service.MongoUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
@RequiredArgsConstructor
public class JwtTokenCustomizer {

    private final MongoUserDetailsService mongoUserDetailsService;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            // Aggiungi claim personalizzati al JWT
            String principalName = context.getPrincipal().getName();

            // Recupera l'utente dal database per ottenere informazioni aggiuntive
            Utente utente = mongoUserDetailsService.getUtenteByUsername(principalName);

            if (utente != null) {
                context.getClaims().claim("username", utente.getUsername());
                context.getClaims().claim("email", utente.getEmail());
            } else {
                // Se non troviamo l'utente, usiamo il principal name come username
                context.getClaims().claim("username", principalName);
            }
        };
    }
}

