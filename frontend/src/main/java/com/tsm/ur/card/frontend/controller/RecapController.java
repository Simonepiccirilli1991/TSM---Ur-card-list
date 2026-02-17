package com.tsm.ur.card.frontend.controller;

import com.tsm.ur.card.frontend.model.BaseRecap;
import com.tsm.ur.card.frontend.model.UserSession;
import com.tsm.ur.card.frontend.service.RecapService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recap")
@RequiredArgsConstructor
@Slf4j
public class RecapController {

    private final RecapService recapService;

    @GetMapping
    public String recapPage(@RequestParam(required = false) String tipo,
                            @RequestParam(required = false) String stato,
                            HttpSession session, Model model) {
        UserSession userSession = getUserSession(session);
        if (userSession == null) return "redirect:/login";

        List<BaseRecap> recapList = recapService.getRecap(userSession.getAccessToken());

        // Applica filtri
        if (tipo != null && !tipo.isEmpty()) {
            recapList = recapList.stream()
                    .filter(r -> tipo.equals(r.getTipoProdotto()))
                    .collect(Collectors.toList());
        }
        if (stato != null && !stato.isEmpty()) {
            recapList = recapList.stream()
                    .filter(r -> stato.equals(r.getStato()))
                    .collect(Collectors.toList());
        }

        // Calcola summary
        RecapSummary summary = calculateSummary(recapList);

        model.addAttribute("recapList", recapList);
        model.addAttribute("summary", summary);
        model.addAttribute("selectedTipo", tipo);
        model.addAttribute("selectedStato", stato);
        model.addAttribute("username", userSession.getUsername());
        return "recap/index";
    }

    private RecapSummary calculateSummary(List<BaseRecap> recapList) {
        RecapSummary summary = new RecapSummary();

        double totaleAcquisti = 0;
        double totaleVendite = 0;
        double profittoNetto = 0;
        int venduti = 0;
        int inCollezione = 0;
        int inVendita = 0;

        for (BaseRecap item : recapList) {
            if (item.getPrezzoAcquisto() != null) {
                totaleAcquisti += item.getPrezzoAcquisto();
            }

            if ("venduto".equals(item.getStato())) {
                venduti++;
                if (item.getPrezzoVendita() != null) {
                    totaleVendite += item.getPrezzoVendita();
                }
                if (item.getNetto() != null) {
                    profittoNetto += item.getNetto();
                }
            } else if ("in_collezione".equals(item.getStato())) {
                inCollezione++;
            } else if ("in_vendita".equals(item.getStato())) {
                inVendita++;
            }
        }

        summary.setTotaleAcquisti(totaleAcquisti);
        summary.setTotaleVendite(totaleVendite);
        summary.setProfittoNetto(profittoNetto);
        summary.setTotaleProdotti(recapList.size());
        summary.setProdottiVenduti(venduti);
        summary.setProdottiInCollezione(inCollezione);
        summary.setProdottiInVendita(inVendita);

        return summary;
    }

    private UserSession getUserSession(HttpSession session) {
        return (UserSession) session.getAttribute("userSession");
    }

    // Inner class per summary
    @lombok.Data
    public static class RecapSummary {
        private double totaleAcquisti;
        private double totaleVendite;
        private double profittoNetto;
        private int totaleProdotti;
        private int prodottiVenduti;
        private int prodottiInCollezione;
        private int prodottiInVendita;
    }
}

