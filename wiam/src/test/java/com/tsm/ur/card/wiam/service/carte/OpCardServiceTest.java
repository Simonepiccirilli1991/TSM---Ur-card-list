package com.tsm.ur.card.wiam.service.carte;

import com.tsm.ur.card.wiam.entity.CartaOnePiece;
import com.tsm.ur.card.wiam.except.OnePieceException;
import com.tsm.ur.card.wiam.model.request.AggiungiOnePieceCardRequest;
import com.tsm.ur.card.wiam.model.request.CancellaCartaOpRequest;
import com.tsm.ur.card.wiam.repository.CardOnePieceRepo;
import com.tsm.ur.card.wiam.util.CarteUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OpCardService Tests")
class OpCardServiceTest {

    @Mock
    private CardOnePieceRepo cardOnePieceRepo;

    @Mock
    private CarteUtil carteUtil;

    @InjectMocks
    private OpCardService opCardService;

    private CartaOnePiece cartaOnePiece;

    @BeforeEach
    void setUp() {
        cartaOnePiece = new CartaOnePiece();
        cartaOnePiece.setId("OP-CARD-123456-testUser");
        cartaOnePiece.setUsernameAssociato("testUser");
        cartaOnePiece.setNome("Monkey D. Luffy");
        cartaOnePiece.setLingua("ENG");
        cartaOnePiece.setEspansione("OP-01");
        cartaOnePiece.setPrezzoAcquisto(50.0);
        cartaOnePiece.setStatoCarta("mint");
        cartaOnePiece.setStato("DISPONIBILE");
        cartaOnePiece.setStatoAcquisto("ACQUISTATO");
    }

    @Test
    @DisplayName("aggiungiCartaOP - Successo")
    void aggiungiCartaOP_Success() {
        // Given
        var request = mock(AggiungiOnePieceCardRequest.class);
        var mappedEntity = new CartaOnePiece();
        mappedEntity.setUsernameAssociato("testUser");
        mappedEntity.setNome("Roronoa Zoro");

        when(request.mappaRequestToEntity()).thenReturn(mappedEntity);
        when(carteUtil.generaIdCartaOnePiece("testUser")).thenReturn("OP-CARD-NEW-testUser");
        when(cardOnePieceRepo.save(any(CartaOnePiece.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        var response = opCardService.aggiungiCartaOP(request);

        // Then
        assertNotNull(response);
        assertEquals("Carta one piece salvata con successo", response.messaggio());
        assertNotNull(response.cartaOnePiece());
        verify(request, times(1)).validaRequest();
        verify(request, times(1)).validaRequestGradata();
        verify(carteUtil, times(1)).generaIdCartaOnePiece("testUser");
        verify(cardOnePieceRepo, times(1)).save(any(CartaOnePiece.class));
    }

    @Test
    @DisplayName("cancellaCartaOP - Successo")
    void cancellaCartaOP_Success() {
        // Given
        var request = new CancellaCartaOpRequest("OP-CARD-123456-testUser", "testUser");
        when(cardOnePieceRepo.findById("OP-CARD-123456-testUser")).thenReturn(Optional.of(cartaOnePiece));

        // When
        var response = opCardService.cancellaCartaOP(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertTrue(response.message().contains("cancellata correttamente"));
        verify(cardOnePieceRepo, times(1)).findById("OP-CARD-123456-testUser");
        verify(cardOnePieceRepo, times(1)).delete(cartaOnePiece);
    }

    @Test
    @DisplayName("cancellaCartaOP - Carta non trovata")
    void cancellaCartaOP_NotFound() {
        // Given
        var request = new CancellaCartaOpRequest("OP-CARD-NONEXISTENT", "testUser");
        when(cardOnePieceRepo.findById("OP-CARD-NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> opCardService.cancellaCartaOP(request));
        verify(cardOnePieceRepo, times(1)).findById("OP-CARD-NONEXISTENT");
        verify(cardOnePieceRepo, never()).delete(any());
    }

    @Test
    @DisplayName("cancellaCartaOP - Utente non autorizzato")
    void cancellaCartaOP_Unauthorized() {
        // Given
        var request = new CancellaCartaOpRequest("OP-CARD-123456-testUser", "differentUser");
        when(cardOnePieceRepo.findById("OP-CARD-123456-testUser")).thenReturn(Optional.of(cartaOnePiece));

        // When & Then
        assertThrows(OnePieceException.class, () -> opCardService.cancellaCartaOP(request));
        verify(cardOnePieceRepo, times(1)).findById("OP-CARD-123456-testUser");
        verify(cardOnePieceRepo, never()).delete(any());
    }

    @Test
    @DisplayName("getCartaOPById - Successo")
    void getCartaOPById_Success() {
        // Given
        when(cardOnePieceRepo.findById("OP-CARD-123456-testUser")).thenReturn(Optional.of(cartaOnePiece));

        // When
        var result = opCardService.getCartaOPById("OP-CARD-123456-testUser");

        // Then
        assertNotNull(result);
        assertEquals("Monkey D. Luffy", result.getNome());
        assertEquals("testUser", result.getUsernameAssociato());
    }

    @Test
    @DisplayName("getCartaOPById - Non trovata")
    void getCartaOPById_NotFound() {
        // Given
        when(cardOnePieceRepo.findById("OP-CARD-NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> opCardService.getCartaOPById("OP-CARD-NONEXISTENT"));
    }

    @Test
    @DisplayName("getCartaOPByUsername - Successo")
    void getCartaOPByUsername_Success() {
        // Given
        when(cardOnePieceRepo.findByUsername("testUser")).thenReturn(List.of(cartaOnePiece));

        // When
        var result = opCardService.getCartaOPByUsername("testUser");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Monkey D. Luffy", result.get(0).getNome());
    }

    @Test
    @DisplayName("getCartaOPByUsername - Lista vuota")
    void getCartaOPByUsername_Empty() {
        // Given
        when(cardOnePieceRepo.findByUsername("emptyUser")).thenReturn(Collections.emptyList());

        // When
        var result = opCardService.getCartaOPByUsername("emptyUser");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getCartaOpByStatoAndUsername - Successo")
    void getCartaOpByStatoAndUsername_Success() {
        // Given
        when(cardOnePieceRepo.findByUsernameAssociatoAndStatoAcquisto("testUser", "ACQUISTATO"))
                .thenReturn(List.of(cartaOnePiece));

        // When
        var result = opCardService.getCartaOpByStatoAndUsername("ACQUISTATO", "testUser");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACQUISTATO", result.get(0).getStatoAcquisto());
    }
}

