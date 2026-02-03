package com.tsm.ur.card.wiam.repository;

import com.tsm.ur.card.wiam.entity.CartaOnePiece;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CardOnePieceRepo extends MongoRepository<CartaOnePiece, String> {
}
