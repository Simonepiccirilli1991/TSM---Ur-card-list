package com.tsm.ur.card.frontend.controller;

import com.tsm.ur.card.frontend.model.CartaOnePiece;
import com.tsm.ur.card.frontend.model.SealedOnePiece;
import com.tsm.ur.card.frontend.model.UserSession;
import com.tsm.ur.card.frontend.model.form.AggiungiCartaOnePieceForm;
import com.tsm.ur.card.frontend.model.form.AggiungiSealedOnePieceForm;
import com.tsm.ur.card.frontend.service.OnePieceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/onepiece")
@RequiredArgsConstructor
@Slf4j
public class OnePieceController {

    private final OnePieceService onePieceService;

    // ===== CARDS =====

    @GetMapping("/cards")
    public String cardsPage(@RequestParam(required = false) String stato,
                           HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        List<CartaOnePiece> cards;
        if (stato != null && !stato.isEmpty()) {
            cards = onePieceService.getMyCardsByStato(userSession.getAccessToken(), stato);
        } else {
            cards = onePieceService.getMyCards(userSession.getAccessToken());
        }

        model.addAttribute("cardList", cards);
        model.addAttribute("selectedStato", stato);
        model.addAttribute("username", userSession.getUsername());
        return "onepiece/cards";
    }

    @GetMapping("/cards/add")
    public String addCardPage(HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        model.addAttribute("form", new AggiungiCartaOnePieceForm());
        model.addAttribute("username", userSession.getUsername());
        return "onepiece/add-card";
    }

    @PostMapping("/cards/add")
    public String addCard(@ModelAttribute AggiungiCartaOnePieceForm form,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        var response = onePieceService.addCard(userSession.getAccessToken(), form);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Carta aggiunta con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    response != null ? response.getMessage() : "Errore durante l'aggiunta");
        }

        return "redirect:/onepiece/cards";
    }

    @GetMapping("/cards/{id}")
    public String cardDetail(@PathVariable String id, HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        CartaOnePiece card = onePieceService.getCardById(userSession.getAccessToken(), id);

        if (card == null) {
            return "redirect:/onepiece/cards";
        }

        model.addAttribute("card", card);
        model.addAttribute("username", userSession.getUsername());
        return "onepiece/card-detail";
    }

    @PostMapping("/cards/{id}/delete")
    public String deleteCard(@PathVariable String id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        var response = onePieceService.deleteCard(userSession.getAccessToken(), id);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Carta eliminata con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Errore durante l'eliminazione");
        }

        return "redirect:/onepiece/cards";
    }

    // ===== SEALED =====

    @GetMapping("/sealed")
    public String sealedPage(@RequestParam(required = false) String stato,
                             HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        List<SealedOnePiece> sealed;
        if (stato != null && !stato.isEmpty()) {
            sealed = onePieceService.getMySealedByStato(userSession.getAccessToken(), stato);
        } else {
            sealed = onePieceService.getMySealed(userSession.getAccessToken());
        }

        model.addAttribute("sealedList", sealed);
        model.addAttribute("selectedStato", stato);
        model.addAttribute("username", userSession.getUsername());
        return "onepiece/sealed";
    }

    @GetMapping("/sealed/add")
    public String addSealedPage(HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        model.addAttribute("form", new AggiungiSealedOnePieceForm());
        model.addAttribute("username", userSession.getUsername());
        return "onepiece/add-sealed";
    }

    @PostMapping("/sealed/add")
    public String addSealed(@ModelAttribute AggiungiSealedOnePieceForm form,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        var response = onePieceService.addSealed(userSession.getAccessToken(), form);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Sealed aggiunto con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    response != null ? response.getMessage() : "Errore durante l'aggiunta");
        }

        return "redirect:/onepiece/sealed";
    }

    @GetMapping("/sealed/{id}")
    public String sealedDetail(@PathVariable String id, HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        SealedOnePiece sealed = onePieceService.getSealedById(userSession.getAccessToken(), id);

        if (sealed == null) {
            return "redirect:/onepiece/sealed";
        }

        model.addAttribute("sealed", sealed);
        model.addAttribute("username", userSession.getUsername());
        return "onepiece/sealed-detail";
    }

    @PostMapping("/sealed/{id}/delete")
    public String deleteSealed(@PathVariable String id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        var response = onePieceService.deleteSealed(userSession.getAccessToken(), id);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            redirectAttributes.addFlashAttribute("success", "Sealed eliminato con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Errore durante l'eliminazione");
        }

        return "redirect:/onepiece/sealed";
    }

    private UserSession getUserSession(HttpSession session) {
        return (UserSession) session.getAttribute("userSession");
    }
}

