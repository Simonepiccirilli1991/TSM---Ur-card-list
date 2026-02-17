package com.tsm.ur.card.authserver.service;

import com.tsm.ur.card.authserver.entity.Utente;
import com.tsm.ur.card.authserver.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cerca prima per username, poi per email
        Utente utente = utenteRepository.findByUsername(username)
                .or(() -> utenteRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));

        // Il controllo enrollment è stato rimosso - non è necessario per il login
        // L'utente può accedere anche senza aver completato l'enrollment

        return User.builder()
                .username(utente.getUsername())
                .password(utente.getPassword())
                .roles("USER")
                .build();
    }

    /**
     * Recupera l'utente completo per username (usato per popolare claims JWT)
     */
    public Utente getUtenteByUsername(String username) {
        return utenteRepository.findByUsername(username)
                .or(() -> utenteRepository.findByEmail(username))
                .orElse(null);
    }
}

