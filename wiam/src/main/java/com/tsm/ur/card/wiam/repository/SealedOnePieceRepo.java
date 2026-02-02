package com.tsm.ur.card.wiam.repository;

import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SealedOnePieceRepo extends MongoRepository<SealedOnePiece, String> {

    List<SealedOnePiece> findByUsernameAssociato(String username);

    List<SealedOnePiece> findByUsernameAssociatoAndStatoAcquisto(String username, String statoAcquisto);
}
