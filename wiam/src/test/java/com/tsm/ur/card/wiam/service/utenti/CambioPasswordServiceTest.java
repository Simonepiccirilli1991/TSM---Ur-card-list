package com.tsm.ur.card.wiam.service.utenti;

import com.tsm.ur.card.wiam.entity.Utente;
import com.tsm.ur.card.wiam.except.UtenteException;
import com.tsm.ur.card.wiam.model.request.CambioPswRequest;
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
@DisplayName("CambioPasswordService Tests")
class CambioPasswordServiceTest {

    @Mock
    private UtenteRepo utenteRepo;

    @InjectMocks
    private CambioPasswordService cambioPasswordService;

    private Utente utente;

    @BeforeEach
    void setUp() {
        utente = new Utente();
        utente.setEmail("test@test.com");
        utente.setUsername("testUser");
        utente.setPassword("oldPassword123");
    }

    @Test
    @DisplayName("cambioPswd - Successo")
    void cambioPswd_Success() {
        // Given
        var request = new CambioPswRequest("testUser", "newPassword456");
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));
        when(utenteRepo.save(any(Utente.class))).thenReturn(utente);

        // When
        var response = cambioPasswordService.cambioPswd(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Password cambiata con successo", response.message());
        assertEquals("newPassword456", utente.getPassword());
        verify(utenteRepo, times(1)).findByUsername("testUser");
        verify(utenteRepo, times(1)).save(utente);
    }

    @Test
    @DisplayName("cambioPswd - Utente non trovato")
    void cambioPswd_UserNotFound() {
        // Given
        var request = new CambioPswRequest("unknownUser", "newPassword456");
        when(utenteRepo.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UtenteException.class, () -> cambioPasswordService.cambioPswd(request));
        verify(utenteRepo, times(1)).findByUsername("unknownUser");
        verify(utenteRepo, never()).save(any());
    }

    @Test
    @DisplayName("cambioPswd - Password nuova uguale a vecchia")
    void cambioPswd_SamePassword() {
        // Given
        var request = new CambioPswRequest("testUser", "oldPassword123");
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));

        // When & Then
        assertThrows(UtenteException.class, () -> cambioPasswordService.cambioPswd(request));
        verify(utenteRepo, times(1)).findByUsername("testUser");
        verify(utenteRepo, never()).save(any());
    }

    @Test
    @DisplayName("cambioPswd - Verifica salvataggio password aggiornata")
    void cambioPswd_VerifyPasswordUpdate() {
        // Given
        var request = new CambioPswRequest("testUser", "nuovaPassword789");
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));
        when(utenteRepo.save(any(Utente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        var response = cambioPasswordService.cambioPswd(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        verify(utenteRepo).save(argThat(u -> u.getPassword().equals("nuovaPassword789")));
    }
}

