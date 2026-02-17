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
                    .filter(r -> tipo.equalsIgnoreCase(r.getTipoProdotto()))
                    .collect(Collectors.toList());
        }
        if (stato != null && !stato.isEmpty()) {
            recapList = recapList.stream()
                    .filter(r -> {
                        String itemStato = r.getStato();
                        String itemStatoAcquisto = r.getStatoAcquisto();
                        return switch (stato) {
                            case "in_collezione" -> "acquistato".equals(itemStato) && "disponibile".equals(itemStatoAcquisto);
                            case "in_vendita" -> "acquistato".equals(itemStato) && "non disponibile".equals(itemStatoAcquisto);
                            case "venduto" -> "venduto".equals(itemStato);
                            default -> true;
                        };
                    })
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

            String stato = item.getStato();
            String statoAcquisto = item.getStatoAcquisto();

            // "venduto" = prodotto venduto
            if ("venduto".equals(stato)) {
                venduti++;
                if (item.getPrezzoVendita() != null) {
                    totaleVendite += item.getPrezzoVendita();
                    // Calcolo profitto netto corretto: vendita - acquisto - costi vendita
                    double prezzoAcquisto = item.getPrezzoAcquisto() != null ? item.getPrezzoAcquisto() : 0;
                    double costiVendita = item.getCostiVendita() != null ? item.getCostiVendita() : 0;
                    double nettoCalcolato = item.getPrezzoVendita() - prezzoAcquisto - costiVendita;
                    item.setNetto(nettoCalcolato); // Aggiorna anche l'item per la visualizzazione in tabella
                    profittoNetto += nettoCalcolato;
                }
            }
            // "acquistato" con statoAcquisto "disponibile" = in collezione
            else if ("acquistato".equals(stato) && "disponibile".equals(statoAcquisto)) {
                inCollezione++;
            }
            // "acquistato" con statoAcquisto "non disponibile" ma non venduto = in vendita
            else if ("acquistato".equals(stato) && "non disponibile".equals(statoAcquisto)) {
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

