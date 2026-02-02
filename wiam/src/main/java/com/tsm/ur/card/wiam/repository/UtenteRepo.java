package com.tsm.ur.card.wiam.repository;

import com.tsm.ur.card.wiam.entity.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteRepo extends MongoRepository<Utente,String> {

    Optional<Utente> findByEmail(String email);
    Optional<Utente> findByUsername(String username);
}
