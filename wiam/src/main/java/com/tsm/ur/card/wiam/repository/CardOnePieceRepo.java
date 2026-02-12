package com.tsm.ur.card.wiam.repository;

import com.tsm.ur.card.wiam.entity.CartaOnePiece;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardOnePieceRepo extends MongoRepository<CartaOnePiece, String> {

    List<CartaOnePiece> findByUsername(String username);

    List<CartaOnePiece> findByUsernameAssociatoAndStatoAcquisto(String username, String statoAcquisto);
}
