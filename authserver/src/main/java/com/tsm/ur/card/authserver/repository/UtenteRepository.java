package com.tsm.ur.card.authserver.repository;

import com.tsm.ur.card.authserver.entity.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteRepository extends MongoRepository<Utente, String> {

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByEmail(String email);
}

