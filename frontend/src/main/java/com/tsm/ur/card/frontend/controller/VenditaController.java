package com.tsm.ur.card.frontend.controller;

import com.tsm.ur.card.frontend.model.UserSession;
import com.tsm.ur.card.frontend.model.form.VendiProdottoForm;
import com.tsm.ur.card.frontend.service.VenditaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vendita")
@RequiredArgsConstructor
@Slf4j
public class VenditaController {

    private final VenditaService venditaService;

    @GetMapping("/vendi")
    public String showVendiPage(@RequestParam String idProdotto,
                                @RequestParam String tipoProdotto,
                                @RequestParam String nomeProdotto,
                                HttpSession session,
                                Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        VendiProdottoForm form = new VendiProdottoForm();
        form.setIdProdotto(idProdotto);
        form.setTipoProdotto(tipoProdotto);

        model.addAttribute("form", form);
        model.addAttribute("nomeProdotto", nomeProdotto);
        model.addAttribute("tipoProdotto", tipoProdotto);
        model.addAttribute("username", userSession.getUsername());
        return "vendita/vendi";
    }

    @PostMapping("/vendi")
    public String vendiProdotto(@ModelAttribute VendiProdottoForm form,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        log.info("Vendita prodotto: id={}, tipo={}", form.getIdProdotto(), form.getTipoProdotto());

        var response = venditaService.vendiProdotto(userSession.getAccessToken(), form);

        String redirectUrl = getRedirectUrl(form.getTipoProdotto());

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Prodotto venduto con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    response != null ? response.getMessage() : "Errore durante la vendita");
        }

        return "redirect:" + redirectUrl;
    }

    private String getRedirectUrl(String tipoProdotto) {
        return switch (tipoProdotto.toUpperCase()) {
            case "POKEMON_CARD" -> "/pokemon/cards";
            case "POKEMON_SEALED" -> "/pokemon/sealed";
            case "ONEPIECE_CARD" -> "/onepiece/cards";
            case "ONEPIECE_SEALED" -> "/onepiece/sealed";
            default -> "/dashboard";
        };
    }

    private UserSession getUserSession(HttpSession session) {
        return (UserSession) session.getAttribute("userSession");
    }
}

