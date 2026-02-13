package com.tsm.ur.card.wiam.service.carte;

import com.tsm.ur.card.wiam.entity.CartaPokemon;
import com.tsm.ur.card.wiam.except.PokemonException;
import com.tsm.ur.card.wiam.model.request.AggiungiCartaPokemonRequest;
import com.tsm.ur.card.wiam.model.request.CancellaCartaPokemonRequest;
import com.tsm.ur.card.wiam.repository.CardPokemonRepo;
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
@DisplayName("PokemonCardService Tests")
class PokemonCardServiceTest {

    @Mock
    private CardPokemonRepo cardPokemonRepo;

    @InjectMocks
    private PokemonCardService pokemonCardService;

    private CartaPokemon cartaPokemon;

    @BeforeEach
    void setUp() {
        cartaPokemon = new CartaPokemon();
        cartaPokemon.setId("PKM-CARD-123456-testUser");
        cartaPokemon.setUsernameAssociato("testUser");
        cartaPokemon.setNome("Charizard");
        cartaPokemon.setLingua("ITA");
        cartaPokemon.setEspansione("Base Set");
        cartaPokemon.setPrezzoAcquisto(100.0);
        cartaPokemon.setStatoCarta("mint");
        cartaPokemon.setStato("DISPONIBILE");
        cartaPokemon.setStatoAcquisto("ACQUISTATO");
    }

    @Test
    @DisplayName("aggiungiCartaPokemonResponse - Successo")
    void aggiungiCartaPokemonResponse_Success() {
        // Given
        var request = new AggiungiCartaPokemonRequest(
                "testUser", "Pikachu", "ITA", "Jungle",
                50.0, LocalDateTime.now(), null,
                false, null, null, "mint"
        );
        when(cardPokemonRepo.save(any(CartaPokemon.class))).thenAnswer(invocation -> {
            CartaPokemon c = invocation.getArgument(0);
            c.setId("PKM-CARD-NEW");
            return c;
        });

        // When
        var response = pokemonCardService.aggiungiCartaPokemonResponse(request);

        // Then
        assertNotNull(response);
        assertEquals("Carta pokemon salvata con successo", response.message());
        assertNotNull(response.cartaPokemon());
        verify(cardPokemonRepo, times(1)).save(any(CartaPokemon.class));
    }

    @Test
    @DisplayName("cancellaCartaPokemonById - Successo")
    void cancellaCartaPokemonById_Success() {
        // Given
        var request = new CancellaCartaPokemonRequest("PKM-CARD-123456-testUser", "testUser");
        when(cardPokemonRepo.findById("PKM-CARD-123456-testUser")).thenReturn(Optional.of(cartaPokemon));

        // When
        var response = pokemonCardService.cancellaCartaPokemonById(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Carta pokemon cancellata con successo", response.message());
        verify(cardPokemonRepo, times(1)).findById("PKM-CARD-123456-testUser");
        verify(cardPokemonRepo, times(1)).delete(cartaPokemon);
    }

    @Test
    @DisplayName("cancellaCartaPokemonById - Carta non trovata")
    void cancellaCartaPokemonById_NotFound() {
        // Given
        var request = new CancellaCartaPokemonRequest("PKM-CARD-NONEXISTENT", "testUser");
        when(cardPokemonRepo.findById("PKM-CARD-NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PokemonException.class, () -> pokemonCardService.cancellaCartaPokemonById(request));
        verify(cardPokemonRepo, times(1)).findById("PKM-CARD-NONEXISTENT");
        verify(cardPokemonRepo, never()).delete(any());
    }

    @Test
    @DisplayName("cancellaCartaPokemonById - Utente non autorizzato")
    void cancellaCartaPokemonById_Unauthorized() {
        // Given
        var request = new CancellaCartaPokemonRequest("PKM-CARD-123456-testUser", "differentUser");
        when(cardPokemonRepo.findById("PKM-CARD-123456-testUser")).thenReturn(Optional.of(cartaPokemon));

        // When & Then
        assertThrows(PokemonException.class, () -> pokemonCardService.cancellaCartaPokemonById(request));
        verify(cardPokemonRepo, times(1)).findById("PKM-CARD-123456-testUser");
        verify(cardPokemonRepo, never()).delete(any());
    }

    @Test
    @DisplayName("getCartaPokemonById - Successo")
    void getCartaPokemonById_Success() {
        // Given
        when(cardPokemonRepo.findById("PKM-CARD-123456-testUser")).thenReturn(Optional.of(cartaPokemon));

        // When
        var result = pokemonCardService.getCartaPokemonById("PKM-CARD-123456-testUser");

        // Then
        assertNotNull(result);
        assertEquals("Charizard", result.getNome());
        assertEquals("testUser", result.getUsernameAssociato());
        verify(cardPokemonRepo, times(1)).findById("PKM-CARD-123456-testUser");
    }

    @Test
    @DisplayName("getCartaPokemonById - Non trovata")
    void getCartaPokemonById_NotFound() {
        // Given
        when(cardPokemonRepo.findById("PKM-CARD-NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PokemonException.class, () -> pokemonCardService.getCartaPokemonById("PKM-CARD-NONEXISTENT"));
    }

    @Test
    @DisplayName("getCartePokemonByUsername - Successo")
    void getCartePokemonByUsername_Success() {
        // Given
        when(cardPokemonRepo.findByUsernameAssociato("testUser")).thenReturn(List.of(cartaPokemon));

        // When
        var result = pokemonCardService.getCartePokemonByUsername("testUser");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Charizard", result.get(0).getNome());
    }

    @Test
    @DisplayName("getCartePokemonByUsername - Lista vuota")
    void getCartePokemonByUsername_Empty() {
        // Given
        when(cardPokemonRepo.findByUsernameAssociato("emptyUser")).thenReturn(Collections.emptyList());

        // When
        var result = pokemonCardService.getCartePokemonByUsername("emptyUser");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getCartePokemonByUsernameAndStatoAcquisto - Successo")
    void getCartePokemonByUsernameAndStatoAcquisto_Success() {
        // Given
        when(cardPokemonRepo.findByUsernameAssociatoAndStatoAcquisto("testUser", "ACQUISTATO"))
                .thenReturn(List.of(cartaPokemon));

        // When
        var result = pokemonCardService.getCartePokemonByUsernameAndStatoAcquisto("testUser", "ACQUISTATO");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACQUISTATO", result.get(0).getStatoAcquisto());
    }
}

