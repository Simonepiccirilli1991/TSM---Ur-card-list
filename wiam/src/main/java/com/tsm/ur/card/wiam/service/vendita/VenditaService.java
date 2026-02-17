package com.tsm.ur.card.wiam.service.vendita;

import com.tsm.ur.card.wiam.except.OnePieceException;
import com.tsm.ur.card.wiam.model.BaseResponse;
import com.tsm.ur.card.wiam.model.request.VendiProdottoRequest;
import com.tsm.ur.card.wiam.repository.CardOnePieceRepo;
import com.tsm.ur.card.wiam.repository.CardPokemonRepo;
import com.tsm.ur.card.wiam.repository.SealedOnePieceRepo;
import com.tsm.ur.card.wiam.repository.SealedPokemonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

import static com.tsm.ur.card.wiam.util.WiamCostanti.StatiAcquisto.NON_DISPONIBILE;
import static com.tsm.ur.card.wiam.util.WiamCostanti.Stato.VENDUTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class VenditaService {

    private final CardPokemonRepo cardPokemonRepo;
    private final SealedPokemonRepo sealedPokemonRepo;
    private final CardOnePieceRepo cardOnePieceRepo;
    private final SealedOnePieceRepo sealedOnePieceRepo;

    public BaseResponse vendiProdotto(VendiProdottoRequest request) {
        log.info("VenditaService - vendiProdotto per id: {}, tipo: {}", request.idProdotto(), request.tipoProdotto());

        return switch (request.tipoProdotto().toUpperCase()) {
            case "POKEMON_CARD" -> vendiCartaPokemon(request);
            case "POKEMON_SEALED" -> vendiSealedPokemon(request);
            case "ONEPIECE_CARD" -> vendiCartaOnePiece(request);
            case "ONEPIECE_SEALED" -> vendiSealedOnePiece(request);
            default -> throw new OnePieceException("Tipo prodotto non valido: " + request.tipoProdotto(), "400", "WIAM-400");
        };
    }

    private BaseResponse vendiCartaPokemon(VendiProdottoRequest request) {
        var carta = cardPokemonRepo.findById(request.idProdotto())
                .orElseThrow(() -> new OnePieceException("Carta Pokemon non trovata: " + request.idProdotto(), "404", "WIAM-404"));

        if (!carta.getUsernameAssociato().equals(request.username())) {
            throw new OnePieceException("Non autorizzato a vendere questa carta", "403", "WIAM-403");
        }

        carta.setStatoAcquisto(NON_DISPONIBILE);
        carta.setStato(VENDUTO);
        carta.setPrezzoVendita(request.prezzoVendita());
        carta.setCostiVendita(request.costiVendita());
        carta.setNetto(request.prezzoVendita() - (request.costiVendita() != null ? request.costiVendita() : 0));
        carta.setDataVendita(request.dataVendita().format(DateTimeFormatter.ISO_LOCAL_DATE));
        carta.setPiattaformaVendita(request.piattaformaVendita());
        carta.setNote(request.note());

        cardPokemonRepo.save(carta);
        log.info("Carta Pokemon {} venduta con successo", request.idProdotto());
        return new BaseResponse("Carta Pokemon venduta con successo", true);
    }

    private BaseResponse vendiSealedPokemon(VendiProdottoRequest request) {
        var sealed = sealedPokemonRepo.findById(request.idProdotto())
                .orElseThrow(() -> new OnePieceException("Sealed Pokemon non trovato: " + request.idProdotto(), "404", "WIAM-404"));

        if (!sealed.getUsernameAssociato().equals(request.username())) {
            throw new OnePieceException("Non autorizzato a vendere questo sealed", "403", "WIAM-403");
        }

        sealed.setStatoAcquisto(NON_DISPONIBILE);
        sealed.setStato(VENDUTO);
        sealed.setPrezzoVendita(request.prezzoVendita());
        sealed.setCostiVendita(request.costiVendita());
        sealed.setNetto(request.prezzoVendita() - (request.costiVendita() != null ? request.costiVendita() : 0));
        sealed.setDataVendita(request.dataVendita().format(DateTimeFormatter.ISO_LOCAL_DATE));
        sealed.setPiattaformaVendita(request.piattaformaVendita());
        sealed.setNote(request.note());

        sealedPokemonRepo.save(sealed);
        log.info("Sealed Pokemon {} venduto con successo", request.idProdotto());
        return new BaseResponse("Sealed Pokemon venduto con successo", true);
    }

    private BaseResponse vendiCartaOnePiece(VendiProdottoRequest request) {
        var carta = cardOnePieceRepo.findById(request.idProdotto())
                .orElseThrow(() -> new OnePieceException("Carta One Piece non trovata: " + request.idProdotto(), "404", "WIAM-404"));

        if (!carta.getUsernameAssociato().equals(request.username())) {
            throw new OnePieceException("Non autorizzato a vendere questa carta", "403", "WIAM-403");
        }

        carta.setStatoAcquisto(NON_DISPONIBILE);
        carta.setStato(VENDUTO);
        carta.setPrezzoVendita(request.prezzoVendita());
        carta.setCostiVendita(request.costiVendita());
        carta.setNetto(request.prezzoVendita() - (request.costiVendita() != null ? request.costiVendita() : 0));
        carta.setDataVendita(request.dataVendita().format(DateTimeFormatter.ISO_LOCAL_DATE));
        carta.setPiattaformaVendita(request.piattaformaVendita());
        carta.setNote(request.note());

        cardOnePieceRepo.save(carta);
        log.info("Carta One Piece {} venduta con successo", request.idProdotto());
        return new BaseResponse("Carta One Piece venduta con successo", true);
    }

    private BaseResponse vendiSealedOnePiece(VendiProdottoRequest request) {
        var sealed = sealedOnePieceRepo.findById(request.idProdotto())
                .orElseThrow(() -> new OnePieceException("Sealed One Piece non trovato: " + request.idProdotto(), "404", "WIAM-404"));

        if (!sealed.getUsernameAssociato().equals(request.username())) {
            throw new OnePieceException("Non autorizzato a vendere questo sealed", "403", "WIAM-403");
        }

        sealed.setStatoAcquisto(NON_DISPONIBILE);
        sealed.setStato(VENDUTO);
        sealed.setPrezzoVendita(request.prezzoVendita());
        sealed.setCostiVendita(request.costiVendita());
        sealed.setNetto(request.prezzoVendita() - (request.costiVendita() != null ? request.costiVendita() : 0));
        sealed.setDataVendita(request.dataVendita().format(DateTimeFormatter.ISO_LOCAL_DATE));
        sealed.setPiattaformaVendita(request.piattaformaVendita());
        sealed.setNote(request.note());

        sealedOnePieceRepo.save(sealed);
        log.info("Sealed One Piece {} venduto con successo", request.idProdotto());
        return new BaseResponse("Sealed One Piece venduto con successo", true);
    }
}
