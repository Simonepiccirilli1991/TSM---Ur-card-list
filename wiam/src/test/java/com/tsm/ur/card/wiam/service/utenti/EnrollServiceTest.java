package com.tsm.ur.card.wiam.service.utenti;

import com.tsm.ur.card.wiam.entity.Utente;
import com.tsm.ur.card.wiam.except.UtenteException;
import com.tsm.ur.card.wiam.repository.UtenteRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnrollService Tests")
class EnrollServiceTest {

    @Mock
    private UtenteRepo utenteRepo;

    @InjectMocks
    private EnrollService enrollService;

    private Utente utente;

    @BeforeEach
    void setUp() {
        utente = new Utente();
        utente.setEmail("test@test.com");
        utente.setUsername("testUser");
        utente.setPassword("password123");
        utente.setEnrollment(false);
        utente.setOtpCount(0);
        utente.setOtpLastTime(LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Test
    @DisplayName("enrollUtente - Successo")
    void enrollUtente_Success() {
        // Given
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));

        // When
        var response = enrollService.enrollUtente("testUser");

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Utente testUser enrollato con successo", response.message());
        assertTrue(utente.getEnrollment());
        verify(utenteRepo, times(1)).findByUsername("testUser");
    }

    @Test
    @DisplayName("enrollUtente - Utente non trovato")
    void enrollUtente_UserNotFound() {
        // Given
        when(utenteRepo.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UtenteException.class, () -> enrollService.enrollUtente("unknownUser"));
        verify(utenteRepo, times(1)).findByUsername("unknownUser");
    }

    @Test
    @DisplayName("aumentaContatoreOtpEnroll - Successo primo OTP")
    void aumentaContatoreOtpEnroll_Success_FirstOtp() {
        // Given
        utente.setOtpCount(0);
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));

        // When
        var response = enrollService.aumentaContatoreOtpEnroll("testUser");

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals(1, utente.getOtpCount());
    }

    @Test
    @DisplayName("aumentaContatoreOtpEnroll - Incremento contatore")
    void aumentaContatoreOtpEnroll_IncrementCounter() {
        // Given
        utente.setOtpCount(3);
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));

        // When
        var response = enrollService.aumentaContatoreOtpEnroll("testUser");

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals(4, utente.getOtpCount());
    }

    @Test
    @DisplayName("aumentaContatoreOtpEnroll - Reset contatore a 1 quando raggiunge 5 e giorno passato")
    void aumentaContatoreOtpEnroll_ResetCounter() {
        // Given
        utente.setOtpCount(5);
        utente.setOtpLastTime(LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));

        // When
        var response = enrollService.aumentaContatoreOtpEnroll("testUser");

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals(1, utente.getOtpCount());
    }

    @Test
    @DisplayName("aumentaContatoreOtpEnroll - Limite raggiunto oggi")
    void aumentaContatoreOtpEnroll_LimitReached() {
        // Given
        utente.setOtpCount(5);
        utente.setOtpLastTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        when(utenteRepo.findByUsername("testUser")).thenReturn(Optional.of(utente));

        // When & Then
        assertThrows(UtenteException.class, () -> enrollService.aumentaContatoreOtpEnroll("testUser"));
    }

    @Test
    @DisplayName("aumentaContatoreOtpEnroll - Utente non trovato")
    void aumentaContatoreOtpEnroll_UserNotFound() {
        // Given
        when(utenteRepo.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UtenteException.class, () -> enrollService.aumentaContatoreOtpEnroll("unknownUser"));
    }
}

