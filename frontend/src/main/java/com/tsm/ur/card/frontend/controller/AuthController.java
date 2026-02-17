package com.tsm.ur.card.frontend.controller;

import com.tsm.ur.card.frontend.model.LoginResponse;
import com.tsm.ur.card.frontend.model.RegisterRequest;
import com.tsm.ur.card.frontend.model.UserSession;
import com.tsm.ur.card.frontend.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            @RequestParam(required = false) String registered,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenziali non valide");
        }
        if (logout != null) {
            model.addAttribute("message", "Logout effettuato con successo");
        }
        if (registered != null) {
            model.addAttribute("message", "Registrazione completata! Effettua il login");
        }
        return "auth/login";
    }

    @PostMapping("/auth/login-process")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        log.info("Processo login per: {}", username);

        LoginResponse response = authService.login(username, password);

        if (response != null && response.getAccessToken() != null) {
            // Salva in sessione
            UserSession userSession = new UserSession(username, null, response.getAccessToken());
            session.setAttribute("userSession", userSession);
            session.setAttribute("username", username);

            log.info("Login riuscito per: {}", username);
            return "redirect:/dashboard";
        } else {
            log.warn("Login fallito per: {}", username);
            redirectAttributes.addFlashAttribute("error", "Credenziali non valide");
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String processRegister(@ModelAttribute RegisterRequest request,
                                  RedirectAttributes redirectAttributes) {
        log.info("Processo registrazione per: {}", request.getUsername());

        // Verifica password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "Le password non coincidono");
            return "redirect:/register";
        }

        var response = authService.register(request);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            return "redirect:/login?registered=true";
        } else {
            redirectAttributes.addFlashAttribute("error",
                    response != null ? response.getMessage() : "Errore durante la registrazione");
            return "redirect:/register";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/auth/forgot-password")
    public String processForgotPassword(@RequestParam String username,
                                        @RequestParam String nuovaPassword,
                                        @RequestParam String confirmPassword,
                                        RedirectAttributes redirectAttributes) {
        log.info("Processo recupero password per: {}", username);

        if (!nuovaPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Le password non coincidono");
            return "redirect:/forgot-password";
        }

        var response = authService.recuperoPassword(username, nuovaPassword);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("message", "Password aggiornata con successo");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error",
                    response != null ? response.getMessage() : "Errore durante il recupero");
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}

