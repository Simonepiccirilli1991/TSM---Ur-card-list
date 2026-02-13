package com.tsm.ur.card.wiam.service.recap;

import com.tsm.ur.card.wiam.entity.CartaOnePiece;
import com.tsm.ur.card.wiam.entity.CartaPokemon;
import com.tsm.ur.card.wiam.entity.SealedOnePiece;
import com.tsm.ur.card.wiam.entity.SealedPokemon;
import com.tsm.ur.card.wiam.model.BaseRecap;
import com.tsm.ur.card.wiam.model.request.RecapRequest;
import com.tsm.ur.card.wiam.service.MapperService;
import com.tsm.ur.card.wiam.service.carte.OpCardService;
import com.tsm.ur.card.wiam.service.carte.OpSealedService;
import com.tsm.ur.card.wiam.service.carte.PokemonCardService;
import com.tsm.ur.card.wiam.service.carte.PokemonSealedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecapService Tests")
class RecapServiceTest {

    @Mock
    private OpCardService opCardService;

    @Mock
    private OpSealedService opSealedService;

    @Mock
    private PokemonCardService pokemonCardService;

    @Mock
    private PokemonSealedService pokemonSealedService;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private RecapService recapService;

    private CartaOnePiece cartaOnePiece;
    private SealedOnePiece sealedOnePiece;
    private CartaPokemon cartaPokemon;
    private SealedPokemon sealedPokemon;
    private BaseRecap baseRecap;

    @BeforeEach
    void setUp() {
        cartaOnePiece = new CartaOnePiece();
        cartaOnePiece.setId("OP-CARD-123");
        cartaOnePiece.setUsernameAssociato("testUser");
        cartaOnePiece.setNome("Luffy");

        sealedOnePiece = new SealedOnePiece();
        sealedOnePiece.setId("OP-SEALED-123");
        sealedOnePiece.setUsernameAssociato("testUser");
        sealedOnePiece.setNome("Booster Box OP-01");

        cartaPokemon = new CartaPokemon();
        cartaPokemon.setId("PKM-CARD-123");
        cartaPokemon.setUsernameAssociato("testUser");
        cartaPokemon.setNome("Charizard");

        sealedPokemon = new SealedPokemon();
        sealedPokemon.setId("PKM-SEALED-123");
        sealedPokemon.setUsernameAssociato("testUser");
        sealedPokemon.setNome("ETB Paldea");

        baseRecap = new BaseRecap();
        baseRecap.setId("TEST-ID");
        baseRecap.setUsernameAssociato("testUser");
    }

    @Test
    @DisplayName("getRecap - ONEPIECE con stato")
    void getRecap_OnePiece_WithStato() {
        // Given
        var request = new RecapRequest("testUser", "ACQUISTATO", "ONEPIECE");
        when(opCardService.getCartaOpByStatoAndUsername("ACQUISTATO", "testUser"))
                .thenReturn(List.of(cartaOnePiece));
        when(opSealedService.getSealedOnePieceByUsernameAndStatoAcquisto("testUser", "ACQUISTATO"))
                .thenReturn(List.of(sealedOnePiece));
        when(mapperService.mappaDtoSuRecap(any(), eq(BaseRecap.class))).thenReturn(baseRecap);

        // When
        var result = recapService.getRecap(request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(opCardService, times(1)).getCartaOpByStatoAndUsername("ACQUISTATO", "testUser");
        verify(opSealedService, times(1)).getSealedOnePieceByUsernameAndStatoAcquisto("testUser", "ACQUISTATO");
        verify(pokemonCardService, never()).getCartePokemonByUsername(any());
    }

    @Test
    @DisplayName("getRecap - ONEPIECE senza stato")
    void getRecap_OnePiece_WithoutStato() {
        // Given
        var request = new RecapRequest("testUser", null, "ONEPIECE");
        when(opCardService.getCartaOPByUsername("testUser")).thenReturn(List.of(cartaOnePiece));
        when(opSealedService.getSealedOnePieceByUsername("testUser")).thenReturn(List.of(sealedOnePiece));
        when(mapperService.mappaDtoSuRecap(any(), eq(BaseRecap.class))).thenReturn(baseRecap);

        // When
        var result = recapService.getRecap(request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(opCardService, times(1)).getCartaOPByUsername("testUser");
        verify(opSealedService, times(1)).getSealedOnePieceByUsername("testUser");
    }

    @Test
    @DisplayName("getRecap - POKEMON con stato")
    void getRecap_Pokemon_WithStato() {
        // Given
        var request = new RecapRequest("testUser", "ACQUISTATO", "POKEMON");
        when(pokemonCardService.getCartePokemonByUsernameAndStatoAcquisto("testUser", "ACQUISTATO"))
                .thenReturn(List.of(cartaPokemon));
        when(pokemonSealedService.getSealedPokemonByUsernameAndStatoAcquisto("testUser", "ACQUISTATO"))
                .thenReturn(List.of(sealedPokemon));
        when(mapperService.mappaDtoSuRecap(any(), eq(BaseRecap.class))).thenReturn(baseRecap);

        // When
        var result = recapService.getRecap(request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pokemonCardService, times(1)).getCartePokemonByUsernameAndStatoAcquisto("testUser", "ACQUISTATO");
        verify(pokemonSealedService, times(1)).getSealedPokemonByUsernameAndStatoAcquisto("testUser", "ACQUISTATO");
        verify(opCardService, never()).getCartaOPByUsername(any());
    }

    @Test
    @DisplayName("getRecap - POKEMON senza stato")
    void getRecap_Pokemon_WithoutStato() {
        // Given
        var request = new RecapRequest("testUser", null, "POKEMON");
        when(pokemonCardService.getCartePokemonByUsername("testUser")).thenReturn(List.of(cartaPokemon));
        when(pokemonSealedService.getSealedPokemonByUsername("testUser")).thenReturn(List.of(sealedPokemon));
        when(mapperService.mappaDtoSuRecap(any(), eq(BaseRecap.class))).thenReturn(baseRecap);

        // When
        var result = recapService.getRecap(request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pokemonCardService, times(1)).getCartePokemonByUsername("testUser");
        verify(pokemonSealedService, times(1)).getSealedPokemonByUsername("testUser");
    }

    @Test
    @DisplayName("getRecap - Default (tutti i prodotti)")
    void getRecap_Default_AllProducts() {
        // Given
        var request = new RecapRequest("testUser", null, null);
        when(opCardService.getCartaOPByUsername("testUser")).thenReturn(List.of(cartaOnePiece));
        when(opSealedService.getSealedOnePieceByUsername("testUser")).thenReturn(List.of(sealedOnePiece));
        when(pokemonCardService.getCartePokemonByUsername("testUser")).thenReturn(List.of(cartaPokemon));
        when(pokemonSealedService.getSealedPokemonByUsername("testUser")).thenReturn(List.of(sealedPokemon));
        when(mapperService.mappaDtoSuRecap(any(), eq(BaseRecap.class))).thenReturn(baseRecap);

        // When
        var result = recapService.getRecap(request);

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        verify(opCardService, times(1)).getCartaOPByUsername("testUser");
        verify(opSealedService, times(1)).getSealedOnePieceByUsername("testUser");
        verify(pokemonCardService, times(1)).getCartePokemonByUsername("testUser");
        verify(pokemonSealedService, times(1)).getSealedPokemonByUsername("testUser");
    }

    @Test
    @DisplayName("getRecap - Lista vuota")
    void getRecap_EmptyList() {
        // Given
        var request = new RecapRequest("emptyUser", null, "POKEMON");
        when(pokemonCardService.getCartePokemonByUsername("emptyUser")).thenReturn(Collections.emptyList());
        when(pokemonSealedService.getSealedPokemonByUsername("emptyUser")).thenReturn(Collections.emptyList());

        // When
        var result = recapService.getRecap(request);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

