package com.tsm.ur.card.wiam.service.carte;

import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import com.tsm.ur.card.wiam.except.OnePieceException;
import com.tsm.ur.card.wiam.model.request.AggiungiOnePiceSealedRequest;
import com.tsm.ur.card.wiam.model.request.CancellaOpSealedRequest;
import com.tsm.ur.card.wiam.repository.SealedOnePieceRepo;
import com.tsm.ur.card.wiam.util.CarteUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OpSealedService Tests")
class OpSealedServiceTest {

    @Mock
    private SealedOnePieceRepo sealedOnePieceRepo;

    @Mock
    private CarteUtil carteUtil;

    @InjectMocks
    private OpSealedService opSealedService;

    private SealedOnePiece sealedOnePiece;

    @BeforeEach
    void setUp() {
        sealedOnePiece = new SealedOnePiece();
        sealedOnePiece.setId("OP-SEALED-123456-testUser");
        sealedOnePiece.setUsernameAssociato("testUser");
        sealedOnePiece.setNome("Booster Box OP-01");
        sealedOnePiece.setLingua("ENG");
        sealedOnePiece.setPrezzoAcquisto(120.0);
        sealedOnePiece.setStato("DISPONIBILE");
        sealedOnePiece.setStatoAcquisto("ACQUISTATO");
    }

    @Test
    @DisplayName("aggiungiSealedOnePiece - Successo")
    void aggiungiSealedOnePiece_Success() {
        // Given
        var request = new AggiungiOnePiceSealedRequest(
                "testUser", "Booster Box OP-05", "JAP", "OP-05",
                100.0, LocalDateTime.now(), null, null, "CardMarket"
        );
        when(carteUtil.generaIdSealedOnePiece("testUser")).thenReturn("OP-SEALED-NEW-testUser");
        when(sealedOnePieceRepo.save(any(SealedOnePiece.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        var response = opSealedService.aggiungiSealedOnePiece(request);

        // Then
        assertNotNull(response);
        assertEquals("Sealed One Piece aggiunto correttamente", response.messaggio());
        assertNotNull(response.sealed());
        verify(carteUtil, times(1)).generaIdSealedOnePiece("testUser");
        verify(sealedOnePieceRepo, times(1)).save(any(SealedOnePiece.class));
    }

    @Test
    @DisplayName("cancellaSealedOnePiece - Successo")
    void cancellaSealedOnePiece_Success() {
        // Given
        var request = new CancellaOpSealedRequest("testUser", "OP-SEALED-123456-testUser");
        when(sealedOnePieceRepo.findById("OP-SEALED-123456-testUser")).thenReturn(Optional.of(sealedOnePiece));

        // When
        var response = opSealedService.cancellaSealedOnePiece(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Sealed One Piece cancellato correttamente", response.message());
        verify(sealedOnePieceRepo, times(1)).findById("OP-SEALED-123456-testUser");
        verify(sealedOnePieceRepo, times(1)).delete(sealedOnePiece);
    }

    @Test
    @DisplayName("cancellaSealedOnePiece - Non trovato")
    void cancellaSealedOnePiece_NotFound() {
        // Given
        var request = new CancellaOpSealedRequest("testUser", "OP-SEALED-NONEXISTENT");
        when(sealedOnePieceRepo.findById("OP-SEALED-NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OnePieceException.class, () -> opSealedService.cancellaSealedOnePiece(request));
        verify(sealedOnePieceRepo, times(1)).findById("OP-SEALED-NONEXISTENT");
        verify(sealedOnePieceRepo, never()).delete(any());
    }

    @Test
    @DisplayName("cancellaSealedOnePiece - Utente non autorizzato")
    void cancellaSealedOnePiece_Unauthorized() {
        // Given
        var request = new CancellaOpSealedRequest("differentUser", "OP-SEALED-123456-testUser");
        when(sealedOnePieceRepo.findById("OP-SEALED-123456-testUser")).thenReturn(Optional.of(sealedOnePiece));

        // When & Then
        assertThrows(OnePieceException.class, () -> opSealedService.cancellaSealedOnePiece(request));
        verify(sealedOnePieceRepo, times(1)).findById("OP-SEALED-123456-testUser");
        verify(sealedOnePieceRepo, never()).delete(any());
    }

    @Test
    @DisplayName("getSealedOnePieceById - Successo")
    void getSealedOnePieceById_Success() {
        // Given
        when(sealedOnePieceRepo.findById("OP-SEALED-123456-testUser")).thenReturn(Optional.of(sealedOnePiece));

        // When
        var result = opSealedService.getSealedOnePieceById("OP-SEALED-123456-testUser");

        // Then
        assertNotNull(result);
        assertEquals("Booster Box OP-01", result.getNome());
        assertEquals("testUser", result.getUsernameAssociato());
    }

    @Test
    @DisplayName("getSealedOnePieceById - Non trovato")
    void getSealedOnePieceById_NotFound() {
        // Given
        when(sealedOnePieceRepo.findById("OP-SEALED-NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OnePieceException.class, () -> opSealedService.getSealedOnePieceById("OP-SEALED-NONEXISTENT"));
    }

    @Test
    @DisplayName("getSealedOnePieceByUsername - Successo")
    void getSealedOnePieceByUsername_Success() {
        // Given
        when(sealedOnePieceRepo.findByUsernameAssociato("testUser")).thenReturn(List.of(sealedOnePiece));

        // When
        var result = opSealedService.getSealedOnePieceByUsername("testUser");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Booster Box OP-01", result.get(0).getNome());
    }

    @Test
    @DisplayName("getSealedOnePieceByUsername - Lista vuota")
    void getSealedOnePieceByUsername_Empty() {
        // Given
        when(sealedOnePieceRepo.findByUsernameAssociato("emptyUser")).thenReturn(Collections.emptyList());

        // When
        var result = opSealedService.getSealedOnePieceByUsername("emptyUser");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getSealedOnePieceByUsernameAndStatoAcquisto - Successo")
    void getSealedOnePieceByUsernameAndStatoAcquisto_Success() {
        // Given
        when(sealedOnePieceRepo.findByUsernameAssociatoAndStatoAcquisto("testUser", "ACQUISTATO"))
                .thenReturn(List.of(sealedOnePiece));

        // When
        var result = opSealedService.getSealedOnePieceByUsernameAndStatoAcquisto("testUser", "ACQUISTATO");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACQUISTATO", result.get(0).getStatoAcquisto());
    }
}

