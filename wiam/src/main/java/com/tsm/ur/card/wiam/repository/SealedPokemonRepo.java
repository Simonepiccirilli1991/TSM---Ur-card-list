package com.tsm.ur.card.wiam.repository;

import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import com.tsm.ur.card.wiam.entity.SealedPokemon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SealedPokemonRepo extends MongoRepository<SealedPokemon, String> {

    List<SealedPokemon> findByUsernameAssociato(String username);

    List<SealedPokemon> findByUsernameAssociatoAndStatoAcquisto(String username, String statoAcquisto);
}
