package com.tsm.ur.card.wiam.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarteUtil Tests")
class CarteUtilTest {

    @InjectMocks
    private CarteUtil carteUtil;

    @Test
    @DisplayName("generaIdCartaPokemon - formato corretto")
    void generaIdCartaPokemon_CorrectFormat() {
        // When
        var result = carteUtil.generaIdCartaPokemon("testUser");

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("PKM-CARD-"));
        assertTrue(result.endsWith("-testUser"));
        assertTrue(result.matches("PKM-CARD-\\d+-testUser"));
    }

    @Test
    @DisplayName("generaIdCartaPokemon - ID univoci")
    void generaIdCartaPokemon_UniqueIds() throws InterruptedException {
        // When
        var id1 = carteUtil.generaIdCartaPokemon("user1");
        Thread.sleep(1); // Aspetta per avere timestamp diverso
        var id2 = carteUtil.generaIdCartaPokemon("user1");

        // Then
        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("generaIdSealedPokemon - formato corretto")
    void generaIdSealedPokemon_CorrectFormat() {
        // When
        var result = carteUtil.generaIdSealedPokemon("testUser");

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("PKM-SEALED-"));
        assertTrue(result.endsWith("-testUser"));
        assertTrue(result.matches("PKM-SEALED-\\d+-testUser"));
    }

    @Test
    @DisplayName("generaIdSealedPokemon - ID univoci")
    void generaIdSealedPokemon_UniqueIds() throws InterruptedException {
        // When
        var id1 = carteUtil.generaIdSealedPokemon("user1");
        Thread.sleep(1);
        var id2 = carteUtil.generaIdSealedPokemon("user1");

        // Then
        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("generaIdCartaOnePiece - formato corretto")
    void generaIdCartaOnePiece_CorrectFormat() {
        // When
        var result = carteUtil.generaIdCartaOnePiece("testUser");

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("OP-CARD-"));
        assertTrue(result.endsWith("-testUser"));
        assertTrue(result.matches("OP-CARD-\\d+-testUser"));
    }

    @Test
    @DisplayName("generaIdCartaOnePiece - ID univoci")
    void generaIdCartaOnePiece_UniqueIds() throws InterruptedException {
        // When
        var id1 = carteUtil.generaIdCartaOnePiece("user1");
        Thread.sleep(1);
        var id2 = carteUtil.generaIdCartaOnePiece("user1");

        // Then
        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("generaIdSealedOnePiece - formato corretto")
    void generaIdSealedOnePiece_CorrectFormat() {
        // When
        var result = carteUtil.generaIdSealedOnePiece("testUser");

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("OP-SEALED-"));
        assertTrue(result.endsWith("-testUser"));
        assertTrue(result.matches("OP-SEALED-\\d+-testUser"));
    }

    @Test
    @DisplayName("generaIdSealedOnePiece - ID univoci")
    void generaIdSealedOnePiece_UniqueIds() throws InterruptedException {
        // When
        var id1 = carteUtil.generaIdSealedOnePiece("user1");
        Thread.sleep(1);
        var id2 = carteUtil.generaIdSealedOnePiece("user1");

        // Then
        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("generaId - username diversi generano ID diversi")
    void generaId_DifferentUsernames() {
        // When
        var id1 = carteUtil.generaIdCartaPokemon("user1");
        var id2 = carteUtil.generaIdCartaPokemon("user2");

        // Then
        assertNotEquals(id1, id2);
        assertTrue(id1.endsWith("-user1"));
        assertTrue(id2.endsWith("-user2"));
    }
}

