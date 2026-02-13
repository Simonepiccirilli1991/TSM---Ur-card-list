# AGENTS.md - Implementazione API WIAM su SEOR

## Obiettivo
Questo documento descrive le API esposte dal microservizio **WIAM** che devono essere integrate/chiamate dal microservizio **SEOR** (Service Orchestrator).

SEOR funzionerà come orchestratore che espone le API al frontend e chiama internamente WIAM.

---

## Configurazione WIAM
- **URL Base**: `http://localhost:8081`
- **Database**: MongoDB locale su `mongodb://localhost:27017/tsm-ur-card-list`

---

## 📋 API UTENTE (`/api/v1/utente`)

### 1. Enroll Utente
- **Endpoint WIAM**: `POST /api/v1/utente/enroll/{username}`
- **Path Variable**: `username` (String)
- **Response**: `BaseResponse`
  ```json
  {
    "message": "string",
    "success": boolean
  }
  ```
- **Descrizione**: Attiva l'enrollment di un utente

### 2. Cambio Password
- **Endpoint WIAM**: `POST /api/v1/utente/cambio-password`
- **Request Body**: `CambioPswRequest`
  ```json
  {
    "username": "string",
    "nuovaPassword": "string"
  }
  ```
- **Response**: `BaseResponse`
- **Descrizione**: Modifica la password di un utente

### 3. Recupero Password
- **Endpoint WIAM**: `POST /api/v1/utente/recupero-password`
- **Request Body**: `RecuperoPswRequest`
  ```json
  {
    "username": "string",
    "nuovaPassword": "string"
  }
  ```
- **Response**: `BaseResponse`
- **Descrizione**: Recupera/resetta la password di un utente

### 4. Registrazione Utente
- **Endpoint WIAM**: `POST /api/v1/utente/registra-utente`
- **Request Body**: `RegistraUtenteRequest`
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string"
  }
  ```
- **Response**: `BaseResponse`
- **Descrizione**: Registra un nuovo utente nel sistema

---

## 📋 API POKEMON CARD (`/api/v1/pokemon/card`)

### 5. Aggiungi Carta Pokemon
- **Endpoint WIAM**: `POST /api/v1/pokemon/card/addcard`
- **Request Body**: `AggiungiCartaPokemonRequest`
  ```json
  {
    "usernameAssociato": "string",
    "nome": "string",
    "lingua": "string",
    "espansione": "string",
    "prezzoAcquisto": number,
    "dataAcquisto": "LocalDateTime (ISO format)",
    "foto": "byte[]",
    "gradata": boolean,
    "enteGradazione": "string",
    "votoGradazione": "string",
    "statoCarta": "string" // mint, near mint, excellent, good, light played
  }
  ```
- **Response**: `AggiungiCartaPokemonResponse`
  ```json
  {
    "message": "string",
    "cartaPokemon": { /* CartaPokemon entity */ }
  }
  ```
- **Descrizione**: Aggiunge una nuova carta Pokemon alla collezione

### 6. Cancella Carta Pokemon
- **Endpoint WIAM**: `DELETE /api/v1/pokemon/card/cancellacarta`
- **Request Body**: `CancellaCartaPokemonRequest`
  ```json
  {
    "idCarta": "string",
    "username": "string"
  }
  ```
- **Response**: `BaseResponse`
- **Descrizione**: Elimina una carta Pokemon dalla collezione

### 7. Get Carta Pokemon by ID
- **Endpoint WIAM**: `GET /api/v1/pokemon/card/getcard/{idCarta}`
- **Path Variable**: `idCarta` (String)
- **Response**: `CartaPokemon`
- **Descrizione**: Recupera una carta Pokemon per ID

### 8. Get Carte Pokemon by Username
- **Endpoint WIAM**: `GET /api/v1/pokemon/card/getcardsbyusername/{username}`
- **Path Variable**: `username` (String)
- **Response**: `List<CartaPokemon>`
- **Descrizione**: Recupera tutte le carte Pokemon di un utente

### 9. Get Carte Pokemon by Username e Stato
- **Endpoint WIAM**: `GET /api/v1/pokemon/card/getcardbyUsernameandstato/{username}/{stato}`
- **Path Variables**: `username` (String), `stato` (String)
- **Response**: `List<CartaPokemon>`
- **Descrizione**: Recupera le carte Pokemon di un utente filtrate per stato

---

## 📋 API POKEMON SEALED (`/api/v1/pokemon/card`)

### 10. Aggiungi Sealed Pokemon
- **Endpoint WIAM**: `POST /api/v1/pokemon/card/addsealed`
- **Request Body**: `AggiungiPokemonSealedRequest`
  ```json
  {
    "username": "string",
    "nome": "string",
    "linguea": "string",
    "espansione": "string",
    "prezzoAcquisto": number,
    "dataAcquisto": "LocalDateTime (ISO format)",
    "dataUscitaProdottoUfficiale": "LocalDateTime (ISO format)",
    "foto": "byte[]",
    "acquistatoPresso": "string"
  }
  ```
- **Response**: `AggiungiSealedPkmResponse`
  ```json
  {
    "messaggio": "string",
    "sealedPokemon": { /* SealedPokemon entity */ }
  }
  ```
- **Descrizione**: Aggiunge un nuovo sealed Pokemon

### 11. Cancella Sealed Pokemon
- **Endpoint WIAM**: `DELETE /api/v1/pokemon/card/cancellasealed`
- **Request Body**: `CancellaPokemonSealedRequest`
  ```json
  {
    "username": "string",
    "idSealed": "string"
  }
  ```
- **Response**: `BaseResponse`
- **Descrizione**: Elimina un sealed Pokemon

### 12. Get Sealed Pokemon by ID
- **Endpoint WIAM**: `GET /api/v1/pokemon/card/getsealedbyid/{idSealed}`
- **Path Variable**: `idSealed` (String)
- **Response**: `SealedPokemon`
- **Descrizione**: Recupera un sealed Pokemon per ID

### 13. Get Sealed Pokemon by Username
- **Endpoint WIAM**: `GET /api/v1/pokemon/card/getSealedByUsername/{username}`
- **Path Variable**: `username` (String)
- **Response**: `List<SealedPokemon>`
- **Descrizione**: Recupera tutti i sealed Pokemon di un utente

### 14. Get Sealed Pokemon by Username e Stato
- **Endpoint WIAM**: `GET /api/v1/pokemon/card/getSealedByUsernameAndStato/{username}/{stato}`
- **Path Variables**: `username` (String), `stato` (String)
- **Response**: `List<SealedPokemon>`
- **Descrizione**: Recupera i sealed Pokemon filtrati per stato

---

## 📋 API ONE PIECE SEALED (`/api/v1/onepiece`)

### 15. Aggiungi Sealed One Piece
- **Endpoint WIAM**: `POST /api/v1/onepiece/add-sealed`
- **Request Body**: `AggiungiOnePiceSealedRequest`
  ```json
  {
    "username": "string",
    "nome": "string",
    "linguea": "string",
    "espansione": "string",
    "prezzoAcquisto": number,
    "dataAcquisto": "LocalDateTime (ISO format)",
    "dataUscitaProdottoUfficiale": "LocalDateTime (ISO format)",
    "foto": "byte[]",
    "acquistatoPresso": "string"
  }
  ```
- **Response**: `AggiungiSealedOPResponse`
  ```json
  {
    "messaggio": "string",
    "sealed": { /* SealedOnePiece entity */ }
  }
  ```
- **Descrizione**: Aggiunge un nuovo sealed One Piece

### 16. Cancella Sealed One Piece
- **Endpoint WIAM**: `POST /api/v1/onepiece/delete-sealed`
- **Request Body**: `CancellaOpSealedRequest`
  ```json
  {
    "username": "string",
    "idSealed": "string"
  }
  ```
- **Response**: `BaseResponse`
- **Descrizione**: Elimina un sealed One Piece

### 17. Get Sealed One Piece by ID
- **Endpoint WIAM**: `GET /api/v1/onepiece/get-sealed/{idSealed}`
- **Path Variable**: `idSealed` (String)
- **Response**: `SealedOnePiece`
- **Descrizione**: Recupera un sealed One Piece per ID

### 18. Get Sealed One Piece by Username
- **Endpoint WIAM**: `GET /api/v1/onepiece/get-sealed-by-user/{username}`
- **Path Variable**: `username` (String)
- **Response**: `List<SealedOnePiece>`
- **Descrizione**: Recupera tutti i sealed One Piece di un utente

### 19. Get Sealed One Piece by Username e Stato
- **Endpoint WIAM**: `GET /api/v1/onepiece/get-sealed-bystato/{username}/{stato}`
- **Path Variables**: `username` (String), `stato` (String)
- **Response**: `List<SealedOnePiece>`
- **Descrizione**: Recupera i sealed One Piece filtrati per stato

---

## 📋 API RECAP (`/api/v1/recap`)

### 20. Get Recap
- **Endpoint WIAM**: `POST /api/v1/recap/getrecap`
- **Request Body**: `RecapRequest`
  ```json
  {
    "username": "string",
    "stato": "string",
    "tipoProdotto": "string"
  }
  ```
- **Response**: `List<BaseRecap>`
- **Descrizione**: Recupera un recap delle collezioni filtrato

---

## 🏗️ IMPLEMENTAZIONE SU SEOR

### Struttura suggerita per SEOR

```
seor/
├── src/main/java/com/tsm/ur/card/seor/
│   ├── controller/
│   │   ├── UtenteController.java       # Espone API utente al frontend
│   │   ├── PokemonController.java      # Espone API Pokemon al frontend
│   │   ├── OnePieceController.java     # Espone API One Piece al frontend
│   │   └── RecapController.java        # Espone API Recap al frontend
│   ├── service/
│   │   ├── wiam/
│   │   │   └── WiamIntegration.java    # Client WebFlux per chiamare WIAM
│   │   ├── UtenteService.java
│   │   ├── PokemonService.java
│   │   ├── OnePieceService.java
│   │   └── RecapService.java
│   ├── model/
│   │   ├── request/                    # Request DTOs (copia da WIAM)
│   │   ├── response/                   # Response DTOs (copia da WIAM)
│   │   └── entity/                     # Entity per mapping (copia da WIAM)
│   ├── config/
│   │   └── WebClientConfig.java        # Configurazione WebClient
│   └── exception/
│       └── SeorExceptionHandler.java   # Gestione errori
└── src/main/resources/
    └── application.yaml                # Configurazione con URL WIAM
```

### Configurazione application.yaml suggerita

```yaml
spring:
  application:
    name: seor

server:
  port: 8080

wiam:
  base-url: http://localhost:8081
  timeout: 30000
```

### Esempio WiamIntegration.java

```java
@Service
@RequiredArgsConstructor
public class WiamIntegration {

    private final WebClient webClient;

    // Utente APIs
    public Mono<BaseResponse> enrollUtente(String username) {
        return webClient.post()
            .uri("/api/v1/utente/enroll/{username}", username)
            .retrieve()
            .bodyToMono(BaseResponse.class);
    }

    public Mono<BaseResponse> cambioPassword(CambioPswRequest request) {
        return webClient.post()
            .uri("/api/v1/utente/cambio-password")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(BaseResponse.class);
    }

    // Pokemon APIs
    public Mono<AggiungiCartaPokemonResponse> aggiungiCartaPokemon(AggiungiCartaPokemonRequest request) {
        return webClient.post()
            .uri("/api/v1/pokemon/card/addcard")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(AggiungiCartaPokemonResponse.class);
    }

    // ... altri metodi per tutte le API
}
```

---

## ✅ CHECKLIST IMPLEMENTAZIONE

### Models da creare su SEOR (copiare da WIAM)
- [x] `BaseResponse.java`
- [x] `BaseRecap.java`
- [x] `CambioPswRequest.java`
- [x] `RecuperoPswRequest.java`
- [x] `RegistraUtenteRequest.java`
- [x] `RecapRequest.java`
- [x] `AggiungiCartaPokemonRequest.java`
- [x] `AggiungiPokemonSealedRequest.java`
- [x] `AggiungiOnePieceSealedRequest.java`
- [x] `CancellaCartaPokemonRequest.java`
- [x] `CancellaPokemonSealedRequest.java`
- [x] `CancellaOnePieceSealedRequest.java`
- [x] `AggiungiCartaPokemonResponse.java`
- [x] `AggiungiSealedPkmResponse.java`
- [x] `AggiungiSealedOPResponse.java`
- [x] `CartaPokemon.java` (DTO)
- [x] `SealedPokemon.java` (DTO)
- [x] `SealedOnePiece.java` (DTO)
- [x] `Utente.java` (DTO)

### Services da implementare su SEOR
- [x] `WiamIntegration.java` - Client per chiamate HTTP a WIAM
- [x] `UtenteService.java` - Logica business utenti
- [x] `PokemonService.java` - Logica business Pokemon
- [x] `OnePieceService.java` - Logica business One Piece
- [x] `RecapService.java` - Logica business Recap

### Controllers da implementare su SEOR
- [x] `UtenteController.java` - 4 endpoint
- [x] `PokemonController.java` - 10 endpoint
- [x] `OnePieceController.java` - 5 endpoint
- [x] `RecapController.java` - 1 endpoint

### Configurazioni
- [x] `WebClientConfig.java` - Configurazione WebClient per WIAM
- [x] Aggiornare `application.yaml` con URL WIAM
- [x] `SeorExceptionHandler.java` - Gestione errori globale

---

## 📊 RIEPILOGO ENDPOINT

| Controller | Metodo | Endpoint WIAM | Descrizione |
|------------|--------|---------------|-------------|
| Utente | POST | `/api/v1/utente/enroll/{username}` | Enroll utente |
| Utente | POST | `/api/v1/utente/cambio-password` | Cambio password |
| Utente | POST | `/api/v1/utente/recupero-password` | Recupero password |
| Utente | POST | `/api/v1/utente/registra-utente` | Registra utente |
| Pokemon | POST | `/api/v1/pokemon/card/addcard` | Aggiungi carta |
| Pokemon | DELETE | `/api/v1/pokemon/card/cancellacarta` | Cancella carta |
| Pokemon | GET | `/api/v1/pokemon/card/getcard/{id}` | Get carta by ID |
| Pokemon | GET | `/api/v1/pokemon/card/getcardsbyusername/{username}` | Get carte by user |
| Pokemon | GET | `/api/v1/pokemon/card/getcardbyUsernameandstato/{user}/{stato}` | Get carte filtrate |
| Pokemon | POST | `/api/v1/pokemon/card/addsealed` | Aggiungi sealed |
| Pokemon | DELETE | `/api/v1/pokemon/card/cancellasealed` | Cancella sealed |
| Pokemon | GET | `/api/v1/pokemon/card/getsealedbyid/{id}` | Get sealed by ID |
| Pokemon | GET | `/api/v1/pokemon/card/getSealedByUsername/{username}` | Get sealed by user |
| Pokemon | GET | `/api/v1/pokemon/card/getSealedByUsernameAndStato/{user}/{stato}` | Get sealed filtrati |
| OnePiece | POST | `/api/v1/onepiece/add-sealed` | Aggiungi sealed OP |
| OnePiece | POST | `/api/v1/onepiece/delete-sealed` | Cancella sealed OP |
| OnePiece | GET | `/api/v1/onepiece/get-sealed/{id}` | Get sealed OP by ID |
| OnePiece | GET | `/api/v1/onepiece/get-sealed-by-user/{username}` | Get sealed OP by user |
| OnePiece | GET | `/api/v1/onepiece/get-sealed-bystato/{user}/{stato}` | Get sealed OP filtrati |
| Recap | POST | `/api/v1/recap/getrecap` | Get recap collezione |

**Totale: 20 endpoint da implementare**


