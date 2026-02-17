package com.tsm.ur.card.frontend.controller;

import com.tsm.ur.card.frontend.model.*;
import com.tsm.ur.card.frontend.service.OnePieceService;
import com.tsm.ur.card.frontend.service.PokemonService;
import com.tsm.ur.card.frontend.service.RecapService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final PokemonService pokemonService;
    private final OnePieceService onePieceService;
    private final RecapService recapService;

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("userSession") != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (userSession == null) {
            return "redirect:/login";
        }

        String token = userSession.getAccessToken();

        // Recupera dati per dashboard
        List<CartaPokemon> pokemonCards = pokemonService.getMyCards(token);
        List<SealedPokemon> pokemonSealed = pokemonService.getMySealed(token);
        List<SealedOnePiece> onePieceSealed = onePieceService.getMySealed(token);
        List<BaseRecap> recap = recapService.getRecap(token);

        // Calcola statistiche
        DashboardStats stats = calculateStats(pokemonCards, pokemonSealed, onePieceSealed, recap);

        model.addAttribute("username", userSession.getUsername());
        model.addAttribute("stats", stats);
        model.addAttribute("pokemonCards", pokemonCards);
        model.addAttribute("pokemonSealed", pokemonSealed);
        model.addAttribute("onePieceSealed", onePieceSealed);
        model.addAttribute("recentItems", getRecentItems(recap, 5));
        model.addAttribute("currentPath", "/dashboard");

        return "dashboard/index";
    }

    private DashboardStats calculateStats(List<CartaPokemon> cards,
                                          List<SealedPokemon> sealedPkm,
                                          List<SealedOnePiece> sealedOp,
                                          List<BaseRecap> recap) {
        DashboardStats stats = new DashboardStats();

        // Valore totale collezione (in_collezione)
        double valoreCollezione = 0;
        int prodottiInCollezione = 0;
        int prodottiInVendita = 0;
        int prodottiVenduti = 0;
        double profittoTotale = 0;
        double totaleAcquisti = 0;
        double totaleVendite = 0;

        for (BaseRecap item : recap) {
            if (item.getPrezzoAcquisto() != null) {
                totaleAcquisti += item.getPrezzoAcquisto();
            }

            String stato = item.getStato();
            if ("in_collezione".equals(stato)) {
                prodottiInCollezione++;
                if (item.getPrezzoAcquisto() != null) {
                    valoreCollezione += item.getPrezzoAcquisto();
                }
            } else if ("in_vendita".equals(stato)) {
                prodottiInVendita++;
            } else if ("venduto".equals(stato)) {
                prodottiVenduti++;
                if (item.getNetto() != null) {
                    profittoTotale += item.getNetto();
                }
                if (item.getPrezzoVendita() != null) {
                    totaleVendite += item.getPrezzoVendita();
                }
            }
        }

        stats.setValoreCollezione(valoreCollezione);
        stats.setProfittoTotale(profittoTotale);
        stats.setProdottiInCollezione(prodottiInCollezione);
        stats.setProdottiInVendita(prodottiInVendita);
        stats.setProdottiVenduti(prodottiVenduti);
        stats.setTotaleAcquisti(totaleAcquisti);
        stats.setTotaleVendite(totaleVendite);
        stats.setTotaleProdotti(recap.size());

        // Conta per categoria
        stats.setNumeroPokemonCards((int) cards.stream().count());
        stats.setNumeroPokemonSealed((int) sealedPkm.stream().count());
        stats.setNumeroOnePieceSealed((int) sealedOp.stream().count());

        return stats;
    }

    private List<BaseRecap> getRecentItems(List<BaseRecap> recap, int limit) {
        return recap.stream()
                .sorted((a, b) -> {
                    if (a.getDataAcquisto() == null) return 1;
                    if (b.getDataAcquisto() == null) return -1;
                    return b.getDataAcquisto().compareTo(a.getDataAcquisto());
                })
                .limit(limit)
                .toList();
    }
}

