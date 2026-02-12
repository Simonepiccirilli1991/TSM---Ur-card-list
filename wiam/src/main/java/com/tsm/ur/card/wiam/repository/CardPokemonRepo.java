package com.tsm.ur.card.wiam.repository;

import com.tsm.ur.card.wiam.entity.CartaPokemon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardPokemonRepo extends MongoRepository<CartaPokemon, String> {

    List<CartaPokemon> findByUsernameAssociato(String usernameAssociato);

    List<CartaPokemon> findByUsernameAssociatoAndStatoAcquisto(String usernameAssociato, String statoAcquisto);
}
