package com.tsm.ur.card.seor.service.wiam;

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
}
