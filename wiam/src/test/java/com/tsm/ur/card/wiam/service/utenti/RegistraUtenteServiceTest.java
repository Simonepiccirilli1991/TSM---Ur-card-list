package com.tsm.ur.card.wiam.service.utenti;

import com.tsm.ur.card.wiam.entity.Utente;
import com.tsm.ur.card.wiam.except.UtenteException;
import com.tsm.ur.card.wiam.model.request.RegistraUtenteRequest;
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
@DisplayName("RegistraUtenteService Tests")
class RegistraUtenteServiceTest {

    @Mock
    private UtenteRepo utenteRepo;

    @InjectMocks
    private RegistraUtenteService registraUtenteService;

    private Utente utente;

    @BeforeEach
    void setUp() {
        utente = new Utente();
        utente.setEmail("existing@test.com");
        utente.setUsername("existingUser");
        utente.setPassword("password123");
    }

    @Test
    @DisplayName("registraUtente - Successo")
    void registraUtente_Success() {
        // Given
        var request = new RegistraUtenteRequest("newUser", "password123", "new@test.com");
        when(utenteRepo.findByUsername("newUser")).thenReturn(Optional.empty());
        when(utenteRepo.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(utenteRepo.save(any(Utente.class))).thenAnswer(invocation -> {
            Utente u = invocation.getArgument(0);
            u.setEmail("new@test.com");
            return u;
        });

        // When
        var response = registraUtenteService.registraUtente(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Utente registrato con successo", response.message());
        verify(utenteRepo, times(1)).findByUsername("newUser");
        verify(utenteRepo, times(1)).findByEmail("new@test.com");
        verify(utenteRepo, times(1)).save(any(Utente.class));
    }

    @Test
    @DisplayName("registraUtente - Username già esistente")
    void registraUtente_UsernameExists() {
        // Given
        var request = new RegistraUtenteRequest("existingUser", "password123", "new@test.com");
        when(utenteRepo.findByUsername("existingUser")).thenReturn(Optional.of(utente));

        // When
        var response = registraUtenteService.registraUtente(request);

        // Then
        assertNotNull(response);
        assertFalse(response.success());
        assertEquals("Utente già esistente", response.message());
        verify(utenteRepo, times(1)).findByUsername("existingUser");
        verify(utenteRepo, never()).save(any());
    }

    @Test
    @DisplayName("registraUtente - Email già registrata")
    void registraUtente_EmailExists() {
        // Given
        var request = new RegistraUtenteRequest("newUser", "password123", "existing@test.com");
        when(utenteRepo.findByUsername("newUser")).thenReturn(Optional.empty());
        when(utenteRepo.findByEmail("existing@test.com")).thenReturn(Optional.of(utente));

        // When & Then
        assertThrows(UtenteException.class, () -> registraUtenteService.registraUtente(request));
        verify(utenteRepo, times(1)).findByUsername("newUser");
        verify(utenteRepo, times(1)).findByEmail("existing@test.com");
        verify(utenteRepo, never()).save(any());
    }

    @Test
    @DisplayName("registraUtente - Verifica campi entity salvata")
    void registraUtente_VerifyEntityFields() {
        // Given
        var request = new RegistraUtenteRequest("testUser", "testPassword", "test@email.com");
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.empty());
        when(utenteRepo.findByEmail("test@email.com")).thenReturn(Optional.empty());
        when(utenteRepo.save(any(Utente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        registraUtenteService.registraUtente(request);

        // Then
        verify(utenteRepo).save(argThat(u ->
            u.getUsername().equals("testUser") &&
            u.getPassword().equals("testPassword") &&
            u.getEmail().equals("test@email.com") &&
            u.getDataRegistrazione() != null
        ));
    }
}

