package com.tsm.ur.card.wiam.service.carte;

import com.tsm.ur.card.wiam.entity.SealedPokemon;
import com.tsm.ur.card.wiam.except.PokemonException;
import com.tsm.ur.card.wiam.model.request.AggiungiPokemonSealedRequest;
import com.tsm.ur.card.wiam.model.request.CancellaPokemonSealedRequest;
import com.tsm.ur.card.wiam.repository.SealedPokemonRepo;
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
@DisplayName("PokemonSealedService Tests")
class PokemonSealedServiceTest {

    @Mock
    private SealedPokemonRepo sealedPokemonRepo;

    @Mock
    private CarteUtil carteUtil;

    @InjectMocks
    private PokemonSealedService pokemonSealedService;

    private SealedPokemon sealedPokemon;

    @BeforeEach
    void setUp() {
        sealedPokemon = new SealedPokemon();
        sealedPokemon.setId("PKM-SEALED-123456-testUser");
        sealedPokemon.setUsernameAssociato("testUser");
        sealedPokemon.setNome("Booster Box Scarlet & Violet");
        sealedPokemon.setLingua("ENG");
        sealedPokemon.setPrezzoAcquisto(180.0);
        sealedPokemon.setStato("DISPONIBILE");
        sealedPokemon.setStatoAcquisto("ACQUISTATO");
    }

    @Test
    @DisplayName("aggiungiSealedPokm - Successo")
    void aggiungiSealedPokm_Success() {
        // Given
        var request = new AggiungiPokemonSealedRequest(
                "testUser", "ETB Paldea", "ITA", "Scarlet & Violet",
                60.0, LocalDateTime.now(), null, null, "Amazon"
        );
        when(carteUtil.generaIdSealedPokemon("testUser")).thenReturn("PKM-SEALED-NEW-testUser");
        when(sealedPokemonRepo.save(any(SealedPokemon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        var response = pokemonSealedService.aggiungiSealedPokm(request);

        // Then
        assertNotNull(response);
        assertEquals("Aggiunto con successo", response.messaggio());
        assertNotNull(response.sealedPokemon());
        verify(carteUtil, times(1)).generaIdSealedPokemon("testUser");
        verify(sealedPokemonRepo, times(1)).save(any(SealedPokemon.class));
    }

    @Test
    @DisplayName("cancellaSealedPokemonById - Successo")
    void cancellaSealedPokemonById_Success() {
        // Given
        var request = new CancellaPokemonSealedRequest("testUser", "PKM-SEALED-123456-testUser");
        when(sealedPokemonRepo.findById("PKM-SEALED-123456-testUser")).thenReturn(Optional.of(sealedPokemon));

        // When
        var response = pokemonSealedService.cancellaSealedPokemonById(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Sealed Pokemon cancellato correttamente", response.message());
        verify(sealedPokemonRepo, times(1)).findById("PKM-SEALED-123456-testUser");
        verify(sealedPokemonRepo, times(1)).delete(sealedPokemon);
    }

    @Test
    @DisplayName("cancellaSealedPokemonById - Non trovato")
    void cancellaSealedPokemonById_NotFound() {
        // Given
        var request = new CancellaPokemonSealedRequest("testUser", "PKM-SEALED-NONEXISTENT");
        when(sealedPokemonRepo.findById("PKM-SEALED-NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PokemonException.class, () -> pokemonSealedService.cancellaSealedPokemonById(request));
        verify(sealedPokemonRepo, times(1)).findById("PKM-SEALED-NONEXISTENT");
        verify(sealedPokemonRepo, never()).delete(any());
    }

    @Test
    @DisplayName("cancellaSealedPokemonById - Utente non autorizzato")
    void cancellaSealedPokemonById_Unauthorized() {
        // Given
        var request = new CancellaPokemonSealedRequest("differentUser", "PKM-SEALED-123456-testUser");
        when(sealedPokemonRepo.findById("PKM-SEALED-123456-testUser")).thenReturn(Optional.of(sealedPokemon));

        // When & Then
        assertThrows(PokemonException.class, () -> pokemonSealedService.cancellaSealedPokemonById(request));
        verify(sealedPokemonRepo, times(1)).findById("PKM-SEALED-123456-testUser");
        verify(sealedPokemonRepo, never()).delete(any());
    }

    @Test
    @DisplayName("getSealedPokemonById - Successo")
    void getSealedPokemonById_Success() {
        // Given
        when(sealedPokemonRepo.findById("PKM-SEALED-123456-testUser")).thenReturn(Optional.of(sealedPokemon));

        // When
        var result = pokemonSealedService.getSealedPokemonById("PKM-SEALED-123456-testUser");

        // Then
        assertNotNull(result);
        assertEquals("Booster Box Scarlet & Violet", result.getNome());
        assertEquals("testUser", result.getUsernameAssociato());
    }

    @Test
    @DisplayName("getSealedPokemonById - Non trovato")
    void getSealedPokemonById_NotFound() {
        // Given
        when(sealedPokemonRepo.findById("PKM-SEALED-NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PokemonException.class, () -> pokemonSealedService.getSealedPokemonById("PKM-SEALED-NONEXISTENT"));
    }

    @Test
    @DisplayName("getSealedPokemonByUsername - Successo")
    void getSealedPokemonByUsername_Success() {
        // Given
        when(sealedPokemonRepo.findByUsernameAssociato("testUser")).thenReturn(List.of(sealedPokemon));

        // When
        var result = pokemonSealedService.getSealedPokemonByUsername("testUser");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Booster Box Scarlet & Violet", result.get(0).getNome());
    }

    @Test
    @DisplayName("getSealedPokemonByUsername - Lista vuota")
    void getSealedPokemonByUsername_Empty() {
        // Given
        when(sealedPokemonRepo.findByUsernameAssociato("emptyUser")).thenReturn(Collections.emptyList());

        // When
        var result = pokemonSealedService.getSealedPokemonByUsername("emptyUser");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getSealedPokemonByUsernameAndStatoAcquisto - Successo")
    void getSealedPokemonByUsernameAndStatoAcquisto_Success() {
        // Given
        when(sealedPokemonRepo.findByUsernameAssociatoAndStatoAcquisto("testUser", "ACQUISTATO"))
                .thenReturn(List.of(sealedPokemon));

        // When
        var result = pokemonSealedService.getSealedPokemonByUsernameAndStatoAcquisto("testUser", "ACQUISTATO");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACQUISTATO", result.get(0).getStatoAcquisto());
    }
}

