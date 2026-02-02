package com.tsm.ur.card.wiam.repository;

import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SealedOnePieceRepo extends MongoRepository<SealedOnePiece, String> {
}
