package com.tsm.ur.card.frontend.controller;

import com.tsm.ur.card.frontend.model.CartaPokemon;
import com.tsm.ur.card.frontend.model.SealedPokemon;
import com.tsm.ur.card.frontend.model.UserSession;
import com.tsm.ur.card.frontend.model.form.AggiungiCartaPokemonForm;
import com.tsm.ur.card.frontend.model.form.AggiungiSealedPokemonForm;
import com.tsm.ur.card.frontend.service.PokemonService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pokemon")
@RequiredArgsConstructor
@Slf4j
public class PokemonController {

    private final PokemonService pokemonService;

    // ===================== CARTE POKEMON =====================

    @GetMapping("/cards")
    public String cardsPage(@RequestParam(required = false) String stato,
                            HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        List<CartaPokemon> cards;
        if (stato != null && !stato.isEmpty()) {
            cards = pokemonService.getMyCardsByStato(userSession.getAccessToken(), stato);
        } else {
            cards = pokemonService.getMyCards(userSession.getAccessToken());
        }

        model.addAttribute("cards", cards);
        model.addAttribute("selectedStato", stato);
        model.addAttribute("username", userSession.getUsername());
        return "pokemon/cards";
    }

    @GetMapping("/cards/add")
    public String addCardPage(HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        model.addAttribute("form", new AggiungiCartaPokemonForm());
        model.addAttribute("username", userSession.getUsername());
        return "pokemon/add-card";
    }

    @PostMapping("/cards/add")
    public String addCard(@ModelAttribute AggiungiCartaPokemonForm form,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        var response = pokemonService.addCard(userSession.getAccessToken(), form);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Carta aggiunta con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    response != null ? response.getMessage() : "Errore durante l'aggiunta");
        }

        return "redirect:/pokemon/cards";
    }

    @GetMapping("/cards/{id}")
    public String cardDetail(@PathVariable String id, HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        CartaPokemon card = pokemonService.getCardById(userSession.getAccessToken(), id);

        if (card == null) {
            return "redirect:/pokemon/cards";
        }

        model.addAttribute("card", card);
        model.addAttribute("username", userSession.getUsername());
        return "pokemon/card-detail";
    }

    @PostMapping("/cards/{id}/delete")
    public String deleteCard(@PathVariable String id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        var response = pokemonService.deleteCard(userSession.getAccessToken(), id);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Carta eliminata con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Errore durante l'eliminazione");
        }

        return "redirect:/pokemon/cards";
    }

    // ===================== SEALED POKEMON =====================

    @GetMapping("/sealed")
    public String sealedPage(@RequestParam(required = false) String stato,
                             HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        List<SealedPokemon> sealed;
        if (stato != null && !stato.isEmpty()) {
            sealed = pokemonService.getMySealedByStato(userSession.getAccessToken(), stato);
        } else {
            sealed = pokemonService.getMySealed(userSession.getAccessToken());
        }

        model.addAttribute("sealedList", sealed);
        model.addAttribute("selectedStato", stato);
        model.addAttribute("username", userSession.getUsername());
        return "pokemon/sealed";
    }

    @GetMapping("/sealed/add")
    public String addSealedPage(HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        model.addAttribute("form", new AggiungiSealedPokemonForm());
        model.addAttribute("username", userSession.getUsername());
        return "pokemon/add-sealed";
    }

    @PostMapping("/sealed/add")
    public String addSealed(@ModelAttribute AggiungiSealedPokemonForm form,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        var response = pokemonService.addSealed(userSession.getAccessToken(), form);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Sealed aggiunto con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    response != null ? response.getMessage() : "Errore durante l'aggiunta");
        }

        return "redirect:/pokemon/sealed";
    }

    @GetMapping("/sealed/{id}")
    public String sealedDetail(@PathVariable String id, HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        SealedPokemon sealed = pokemonService.getSealedById(userSession.getAccessToken(), id);

        if (sealed == null) {
            return "redirect:/pokemon/sealed";
        }

        model.addAttribute("sealed", sealed);
        model.addAttribute("username", userSession.getUsername());
        return "pokemon/sealed-detail";
    }

    @PostMapping("/sealed/{id}/delete")
    public String deleteSealed(@PathVariable String id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        var response = pokemonService.deleteSealed(userSession.getAccessToken(), id);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Sealed eliminato con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Errore durante l'eliminazione");
        }

        return "redirect:/pokemon/sealed";
    }

    private UserSession getUserSession(HttpSession session) {
        return (UserSession) session.getAttribute("userSession");
    }
}

