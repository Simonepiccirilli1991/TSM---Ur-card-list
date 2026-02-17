package com.tsm.ur.card.seor.service.wiam;

import com.tsm.ur.card.seor.model.dto.CartaOnePiece;
import com.tsm.ur.card.seor.model.dto.CartaPokemon;
import com.tsm.ur.card.seor.model.dto.SealedOnePiece;
import com.tsm.ur.card.seor.model.dto.SealedPokemon;
import com.tsm.ur.card.seor.model.request.*;
import com.tsm.ur.card.seor.model.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WiamIntegration {

    private final WebClient wiamWebClient;

    // ===================== UTENTE APIs =====================

    public BaseResponse enrollUtente(String username) {
        log.info("Chiamata WIAM - enrollUtente per username: {}", username);
        return wiamWebClient.post()
                .uri("/api/v1/utente/enroll/{username}", username)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM enrollUtente response: {}", response))
                .doOnError(error -> log.error("WIAM enrollUtente error: {}", error.getMessage()))
                .block();
    }

    public BaseResponse cambioPassword(CambioPswRequest request) {
        log.info("Chiamata WIAM - cambioPassword per username: {}", request.username());
        return wiamWebClient.post()
                .uri("/api/v1/utente/cambio-password")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM cambioPassword response: {}", response))
                .doOnError(error -> log.error("WIAM cambioPassword error: {}", error.getMessage()))
                .block();
    }

    public BaseResponse cambioPassword(String username, String nuovaPassword) {
        log.info("Chiamata WIAM - cambioPassword per username: {}", username);
        CambioPswRequest request = new CambioPswRequest(username, nuovaPassword);
        return cambioPassword(request);
    }

    public BaseResponse recuperoPassword(RecuperoPswRequest request) {
        log.info("Chiamata WIAM - recuperoPassword per username: {}", request.username());
        return wiamWebClient.post()
                .uri("/api/v1/utente/recupero-password")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM recuperoPassword response: {}", response))
                .doOnError(error -> log.error("WIAM recuperoPassword error: {}", error.getMessage()))
                .block();
    }

    public BaseResponse registraUtente(RegistraUtenteRequest request) {
        log.info("Chiamata WIAM - registraUtente per username: {}", request.username());
        return wiamWebClient.post()
                .uri("/api/v1/utente/registra-utente")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM registraUtente response: {}", response))
                .doOnError(error -> log.error("WIAM registraUtente error: {}", error.getMessage()))
                .block();
    }

    // ===================== POKEMON CARD APIs =====================

    public AggiungiCartaPokemonResponse aggiungiCartaPokemon(AggiungiCartaPokemonRequest request) {
        log.info("Chiamata WIAM - aggiungiCartaPokemon per username: {}", request.usernameAssociato());
        return wiamWebClient.post()
                .uri("/api/v1/pokemon/card/addcard")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AggiungiCartaPokemonResponse.class)
                .doOnSuccess(response -> log.info("WIAM aggiungiCartaPokemon response: {}", response))
                .doOnError(error -> log.error("WIAM aggiungiCartaPokemon error: {}", error.getMessage()))
                .block();
    }

    public AggiungiCartaPokemonResponse aggiungiCartaPokemon(String username, AggiungiCartaPokemonRequest request) {
        log.info("Chiamata WIAM - aggiungiCartaPokemon per username (JWT): {}", username);
        // Crea una nuova request con l'username dal JWT
        AggiungiCartaPokemonRequest wiamRequest = new AggiungiCartaPokemonRequest(
                username,
                request.nome(),
                request.lingua(),
                request.espansione(),
                request.prezzoAcquisto(),
                request.dataAcquisto(),
                request.foto(),
                request.gradata(),
                request.enteGradazione(),
                request.votoGradazione(),
                request.statoCarta()
        );
        return aggiungiCartaPokemon(wiamRequest);
    }

    public BaseResponse cancellaCartaPokemon(CancellaCartaPokemonRequest request) {
        log.info("Chiamata WIAM - cancellaCartaPokemon per idCarta: {}", request.idCarta());
        return wiamWebClient.method(org.springframework.http.HttpMethod.DELETE)
                .uri("/api/v1/pokemon/card/cancellacarta")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM cancellaCartaPokemon response: {}", response))
                .doOnError(error -> log.error("WIAM cancellaCartaPokemon error: {}", error.getMessage()))
                .block();
    }

    public BaseResponse cancellaCartaPokemon(String username, String idCarta) {
        log.info("Chiamata WIAM - cancellaCartaPokemon per username (JWT): {}, idCarta: {}", username, idCarta);
        CancellaCartaPokemonRequest request = new CancellaCartaPokemonRequest(idCarta, username);
        return cancellaCartaPokemon(request);
    }

    public CartaPokemon getCartaPokemonById(String idCarta) {
        log.info("Chiamata WIAM - getCartaPokemonById per id: {}", idCarta);
        return wiamWebClient.get()
                .uri("/api/v1/pokemon/card/getcard/{idCarta}", idCarta)
                .retrieve()
                .bodyToMono(CartaPokemon.class)
                .doOnSuccess(response -> log.info("WIAM getCartaPokemonById response: {}", response))
                .doOnError(error -> log.error("WIAM getCartaPokemonById error: {}", error.getMessage()))
                .block();
    }

    public List<CartaPokemon> getCartePokemonByUsername(String username) {
        log.info("Chiamata WIAM - getCartePokemonByUsername per username: {}", username);
        return wiamWebClient.get()
                .uri("/api/v1/pokemon/card/getcardsbyusername/{username}", username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CartaPokemon>>() {})
                .doOnSuccess(response -> log.info("WIAM getCartePokemonByUsername response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getCartePokemonByUsername error: {}", error.getMessage()))
                .block();
    }

    public List<CartaPokemon> getCartePokemonByUsernameAndStato(String username, String stato) {
        log.info("Chiamata WIAM - getCartePokemonByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamWebClient.get()
                .uri("/api/v1/pokemon/card/getcardbyUsernameandstato/{username}/{stato}", username, stato)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CartaPokemon>>() {})
                .doOnSuccess(response -> log.info("WIAM getCartePokemonByUsernameAndStato response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getCartePokemonByUsernameAndStato error: {}", error.getMessage()))
                .block();
    }

    // ===================== POKEMON SEALED APIs =====================

    public AggiungiSealedPkmResponse aggiungiSealedPokemon(AggiungiPokemonSealedRequest request) {
        log.info("Chiamata WIAM - aggiungiSealedPokemon per username: {}", request.username());
        return wiamWebClient.post()
                .uri("/api/v1/pokemon/card/addsealed")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AggiungiSealedPkmResponse.class)
                .doOnSuccess(response -> log.info("WIAM aggiungiSealedPokemon response: {}", response))
                .doOnError(error -> log.error("WIAM aggiungiSealedPokemon error: {}", error.getMessage()))
                .block();
    }

    public AggiungiSealedPkmResponse aggiungiSealedPokemon(String username, AggiungiPokemonSealedRequest request) {
        log.info("Chiamata WIAM - aggiungiSealedPokemon per username (JWT): {}", username);
        AggiungiPokemonSealedRequest wiamRequest = new AggiungiPokemonSealedRequest(
                username,
                request.nome(),
                request.linguea(),
                request.espansione(),
                request.prezzoAcquisto(),
                request.dataAcquisto(),
                request.dataUscitaProdottoUfficiale(),
                request.foto(),
                request.acquistatoPresso()
        );
        return aggiungiSealedPokemon(wiamRequest);
    }

    public BaseResponse cancellaSealedPokemon(CancellaPokemonSealedRequest request) {
        log.info("Chiamata WIAM - cancellaSealedPokemon per idSealed: {}", request.idSealed());
        return wiamWebClient.method(org.springframework.http.HttpMethod.DELETE)
                .uri("/api/v1/pokemon/card/cancellasealed")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM cancellaSealedPokemon response: {}", response))
                .doOnError(error -> log.error("WIAM cancellaSealedPokemon error: {}", error.getMessage()))
                .block();
    }

    public BaseResponse cancellaSealedPokemon(String username, String idSealed) {
        log.info("Chiamata WIAM - cancellaSealedPokemon per username (JWT): {}, idSealed: {}", username, idSealed);
        CancellaPokemonSealedRequest request = new CancellaPokemonSealedRequest(username, idSealed);
        return cancellaSealedPokemon(request);
    }

    public SealedPokemon getSealedPokemonById(String idSealed) {
        log.info("Chiamata WIAM - getSealedPokemonById per id: {}", idSealed);
        return wiamWebClient.get()
                .uri("/api/v1/pokemon/card/getsealedbyid/{idSealed}", idSealed)
                .retrieve()
                .bodyToMono(SealedPokemon.class)
                .doOnSuccess(response -> log.info("WIAM getSealedPokemonById response: {}", response))
                .doOnError(error -> log.error("WIAM getSealedPokemonById error: {}", error.getMessage()))
                .block();
    }

    public List<SealedPokemon> getSealedPokemonByUsername(String username) {
        log.info("Chiamata WIAM - getSealedPokemonByUsername per username: {}", username);
        return wiamWebClient.get()
                .uri("/api/v1/pokemon/card/getSealedByUsername/{username}", username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SealedPokemon>>() {})
                .doOnSuccess(response -> log.info("WIAM getSealedPokemonByUsername response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getSealedPokemonByUsername error: {}", error.getMessage()))
                .block();
    }

    public List<SealedPokemon> getSealedPokemonByUsernameAndStato(String username, String stato) {
        log.info("Chiamata WIAM - getSealedPokemonByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamWebClient.get()
                .uri("/api/v1/pokemon/card/getSealedByUsernameAndStato/{username}/{stato}", username, stato)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SealedPokemon>>() {})
                .doOnSuccess(response -> log.info("WIAM getSealedPokemonByUsernameAndStato response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getSealedPokemonByUsernameAndStato error: {}", error.getMessage()))
                .block();
    }

    // ===================== ONE PIECE SEALED APIs =====================

    public AggiungiSealedOPResponse aggiungiSealedOnePiece(AggiungiOnePieceSealedRequest request) {
        log.info("Chiamata WIAM - aggiungiSealedOnePiece per username: {}", request.username());
        return wiamWebClient.post()
                .uri("/api/v1/onepiece/add-sealed")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AggiungiSealedOPResponse.class)
                .doOnSuccess(response -> log.info("WIAM aggiungiSealedOnePiece response: {}", response))
                .doOnError(error -> log.error("WIAM aggiungiSealedOnePiece error: {}", error.getMessage()))
                .block();
    }

    public AggiungiSealedOPResponse aggiungiSealedOnePiece(String username, AggiungiOnePieceSealedRequest request) {
        log.info("Chiamata WIAM - aggiungiSealedOnePiece per username (JWT): {}", username);
        AggiungiOnePieceSealedRequest wiamRequest = new AggiungiOnePieceSealedRequest(
                username,
                request.nome(),
                request.linguea(),
                request.espansione(),
                request.prezzoAcquisto(),
                request.dataAcquisto(),
                request.dataUscitaProdottoUfficiale(),
                request.foto(),
                request.acquistatoPresso()
        );
        return aggiungiSealedOnePiece(wiamRequest);
    }

    public BaseResponse cancellaSealedOnePiece(CancellaOnePieceSealedRequest request) {
        log.info("Chiamata WIAM - cancellaSealedOnePiece per idSealed: {}", request.idSealed());
        return wiamWebClient.post()
                .uri("/api/v1/onepiece/delete-sealed")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM cancellaSealedOnePiece response: {}", response))
                .doOnError(error -> log.error("WIAM cancellaSealedOnePiece error: {}", error.getMessage()))
                .block();
    }

    public BaseResponse cancellaSealedOnePiece(String username, String idSealed) {
        log.info("Chiamata WIAM - cancellaSealedOnePiece per username (JWT): {}, idSealed: {}", username, idSealed);
        CancellaOnePieceSealedRequest request = new CancellaOnePieceSealedRequest(username, idSealed);
        return cancellaSealedOnePiece(request);
    }

    public SealedOnePiece getSealedOnePieceById(String idSealed) {
        log.info("Chiamata WIAM - getSealedOnePieceById per id: {}", idSealed);
        return wiamWebClient.get()
                .uri("/api/v1/onepiece/get-sealed/{idSealed}", idSealed)
                .retrieve()
                .bodyToMono(SealedOnePiece.class)
                .doOnSuccess(response -> log.info("WIAM getSealedOnePieceById response: {}", response))
                .doOnError(error -> log.error("WIAM getSealedOnePieceById error: {}", error.getMessage()))
                .block();
    }

    public List<SealedOnePiece> getSealedOnePieceByUsername(String username) {
        log.info("Chiamata WIAM - getSealedOnePieceByUsername per username: {}", username);
        return wiamWebClient.get()
                .uri("/api/v1/onepiece/get-sealed-by-user/{username}", username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SealedOnePiece>>() {})
                .doOnSuccess(response -> log.info("WIAM getSealedOnePieceByUsername response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getSealedOnePieceByUsername error: {}", error.getMessage()))
                .block();
    }

    public List<SealedOnePiece> getSealedOnePieceByUsernameAndStato(String username, String stato) {
        log.info("Chiamata WIAM - getSealedOnePieceByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamWebClient.get()
                .uri("/api/v1/onepiece/get-sealed-bystato/{username}/{stato}", username, stato)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SealedOnePiece>>() {})
                .doOnSuccess(response -> log.info("WIAM getSealedOnePieceByUsernameAndStato response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getSealedOnePieceByUsernameAndStato error: {}", error.getMessage()))
                .block();
    }

    // ===================== ONE PIECE CARD APIs =====================

    public AggiungiCartaOnePieceResponse aggiungiCartaOnePiece(AggiungiCartaOnePieceRequest request) {
        log.info("Chiamata WIAM - aggiungiCartaOnePiece per username: {}", request.usernameAssociato());
        return wiamWebClient.post()
                .uri("/api/v1/onepiece/add-card")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AggiungiCartaOnePieceResponse.class)
                .doOnSuccess(response -> log.info("WIAM aggiungiCartaOnePiece response: {}", response))
                .doOnError(error -> log.error("WIAM aggiungiCartaOnePiece error: {}", error.getMessage()))
                .block();
    }

    public AggiungiCartaOnePieceResponse aggiungiCartaOnePiece(String username, AggiungiCartaOnePieceRequest request) {
        log.info("Chiamata WIAM - aggiungiCartaOnePiece per username (JWT): {}", username);
        AggiungiCartaOnePieceRequest wiamRequest = new AggiungiCartaOnePieceRequest(
                username,
                request.nome(),
                request.lingua(),
                request.espansione(),
                request.prezzoAcquisto(),
                request.dataAcquisto(),
                request.foto(),
                request.gradata(),
                request.enteGradazione(),
                request.votoGradazione(),
                request.statoCarta()
        );
        return aggiungiCartaOnePiece(wiamRequest);
    }

    public BaseResponse cancellaCartaOnePiece(CancellaCartaOnePieceRequest request) {
        log.info("Chiamata WIAM - cancellaCartaOnePiece per idCarta: {}", request.idCarta());
        return wiamWebClient.post()
                .uri("/api/v1/onepiece/delete-card")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM cancellaCartaOnePiece response: {}", response))
                .doOnError(error -> log.error("WIAM cancellaCartaOnePiece error: {}", error.getMessage()))
                .block();
    }

    public BaseResponse cancellaCartaOnePiece(String username, String idCarta) {
        log.info("Chiamata WIAM - cancellaCartaOnePiece per username (JWT): {}, idCarta: {}", username, idCarta);
        CancellaCartaOnePieceRequest request = new CancellaCartaOnePieceRequest(idCarta);
        return cancellaCartaOnePiece(request);
    }

    public CartaOnePiece getCartaOnePieceById(String idCarta) {
        log.info("Chiamata WIAM - getCartaOnePieceById per id: {}", idCarta);
        return wiamWebClient.get()
                .uri("/api/v1/onepiece/get-card/{idCarta}", idCarta)
                .retrieve()
                .bodyToMono(CartaOnePiece.class)
                .doOnSuccess(response -> log.info("WIAM getCartaOnePieceById response: {}", response))
                .doOnError(error -> log.error("WIAM getCartaOnePieceById error: {}", error.getMessage()))
                .block();
    }

    public List<CartaOnePiece> getCarteOnePieceByUsername(String username) {
        log.info("Chiamata WIAM - getCarteOnePieceByUsername per username: {}", username);
        return wiamWebClient.get()
                .uri("/api/v1/onepiece/get-cards-by-user/{username}", username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CartaOnePiece>>() {})
                .doOnSuccess(response -> log.info("WIAM getCarteOnePieceByUsername response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getCarteOnePieceByUsername error: {}", error.getMessage()))
                .block();
    }

    public List<CartaOnePiece> getCarteOnePieceByUsernameAndStato(String username, String stato) {
        log.info("Chiamata WIAM - getCarteOnePieceByUsernameAndStato per username: {}, stato: {}", username, stato);
        return wiamWebClient.get()
                .uri("/api/v1/onepiece/get-cards-bystato/{username}/{stato}", username, stato)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CartaOnePiece>>() {})
                .doOnSuccess(response -> log.info("WIAM getCarteOnePieceByUsernameAndStato response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getCarteOnePieceByUsernameAndStato error: {}", error.getMessage()))
                .block();
    }

    // ===================== RECAP APIs =====================

    public List<BaseRecap> getRecap(RecapRequest request) {
        log.info("Chiamata WIAM - getRecap per username: {}", request.username());
        return wiamWebClient.post()
                .uri("/api/v1/recap/getrecap")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BaseRecap>>() {})
                .doOnSuccess(response -> log.info("WIAM getRecap response count: {}", response != null ? response.size() : 0))
                .doOnError(error -> log.error("WIAM getRecap error: {}", error.getMessage()))
                .block();
    }

    public List<BaseRecap> getRecap(String username) {
        log.info("Chiamata WIAM - getRecap per username (JWT): {}", username);
        RecapRequest request = new RecapRequest(username);
        return getRecap(request);
    }

    // ===================== VENDITA APIs =====================

    public BaseResponse vendiProdotto(VendiProdottoRequest request) {
        log.info("Chiamata WIAM - vendiProdotto per id: {}, tipo: {}", request.idProdotto(), request.tipoProdotto());
        return wiamWebClient.post()
                .uri("/api/v1/vendita/vendi")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .doOnSuccess(response -> log.info("WIAM vendiProdotto response: {}", response))
                .doOnError(error -> log.error("WIAM vendiProdotto error: {}", error.getMessage()))
                .block();
    }

    public BaseResponse vendiProdotto(String username, VendiProdottoRequest request) {
        log.info("Chiamata WIAM - vendiProdotto per username (JWT): {}", username);
        VendiProdottoRequest wiamRequest = new VendiProdottoRequest(
                request.idProdotto(),
                username,
                request.tipoProdotto(),
                request.prezzoVendita(),
                request.costiVendita(),
                request.dataVendita(),
                request.piattaformaVendita(),
                request.note()
        );
        return vendiProdotto(wiamRequest);
    }
}
