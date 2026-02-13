package com.tsm.ur.card.wiam.service.utenti;

import com.tsm.ur.card.wiam.entity.Utente;
import com.tsm.ur.card.wiam.model.request.RecuperoPswRequest;
import com.tsm.ur.card.wiam.repository.UtenteRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecuperoPswService Tests")
class RecuperoPswServiceTest {

    @Mock
    private UtenteRepo utenteRepo;

    @InjectMocks
    private RecuperoPswService recuperoPswService;

    private Utente utente;

    @BeforeEach
    void setUp() {
        utente = new Utente();
        utente.setEmail("test@test.com");
        utente.setUsername("testUser");
        utente.setPassword("oldPassword123");
    }

    @Test
    @DisplayName("recuperaPsw - Successo")
    void recuperaPsw_Success() {
        // Given
        var request = new RecuperoPswRequest("testUser", "newRecoveredPassword");
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));
        when(utenteRepo.save(any(Utente.class))).thenReturn(utente);

        // When
        var response = recuperoPswService.recuperaPsw(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertTrue(response.message().contains("testUser"));
        assertEquals("newRecoveredPassword", utente.getPassword());
        verify(utenteRepo, times(1)).findByUsername("testUser");
        verify(utenteRepo, times(1)).save(utente);
    }

    @Test
    @DisplayName("recuperaPsw - Utente non trovato")
    void recuperaPsw_UserNotFound() {
        // Given
        var request = new RecuperoPswRequest("unknownUser", "newPassword");
        when(utenteRepo.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> recuperoPswService.recuperaPsw(request));
        verify(utenteRepo, times(1)).findByUsername("unknownUser");
        verify(utenteRepo, never()).save(any());
    }

    @Test
    @DisplayName("recuperaPsw - Verifica aggiornamento password")
    void recuperaPsw_VerifyPasswordUpdate() {
        // Given
        var request = new RecuperoPswRequest("testUser", "superSecurePassword123");
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));
        when(utenteRepo.save(any(Utente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        recuperoPswService.recuperaPsw(request);

        // Then
        verify(utenteRepo).save(argThat(u -> u.getPassword().equals("superSecurePassword123")));
    }
}

