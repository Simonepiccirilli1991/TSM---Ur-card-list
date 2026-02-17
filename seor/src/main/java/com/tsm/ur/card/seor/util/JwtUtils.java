package com.tsm.ur.card.seor.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    /**
     * Estrae l'username dal JWT presente nell'Authentication
     *
     * @param authentication L'oggetto Authentication contenente il JWT
     * @return L'username estratto dal claim "username" del JWT
     * @throws IllegalStateException se il principal non è un JWT o se il claim non è presente
     */
    public String extractUsername(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("Authentication is null");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String username = jwt.getClaimAsString("username");
            if (username == null || username.isBlank()) {
                // Fallback: usa il subject del JWT
                username = jwt.getSubject();
            }
            if (username == null || username.isBlank()) {
                throw new IllegalStateException("Cannot extract username from JWT: no username claim or subject found");
            }
            return username;
        }

        throw new IllegalStateException("Cannot extract username from JWT: principal is not a Jwt instance");
    }

    /**
     * Estrae l'username dal JWT presente nel SecurityContext corrente
     *
     * @return L'username estratto dal claim "username" del JWT
     * @throws IllegalStateException se non c'è un'autenticazione valida nel SecurityContext
     */
    public String extractUsernameFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return extractUsername(authentication);
    }

    /**
     * Estrae l'email dal JWT presente nell'Authentication
     *
     * @param authentication L'oggetto Authentication contenente il JWT
     * @return L'email estratta dal claim "email" del JWT, o null se non presente
     */
    public String extractEmail(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("email");
        }
        return null;
    }

    /**
     * Estrae l'email dal JWT presente nel SecurityContext corrente
     *
     * @return L'email estratta dal claim "email" del JWT, o null se non presente
     */
    public String extractEmailFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return extractEmail(authentication);
    }
}

