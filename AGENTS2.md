# AGENTS2.md - Implementazione OAuth2 Authorization Server e Resource Server

## Obiettivo
Questo documento descrive gli step necessari per implementare un sistema OAuth2 che securizzi le API del microservizio **SEOR** utilizzando un **Authorization Server** nel microservizio **authserver** che valida i JWT.

---

## 📋 PANORAMICA ARCHITETTURA

```
┌─────────────────┐     JWT Token      ┌─────────────────┐     API Call      ┌─────────────────┐
│    Frontend     │ ────────────────► │      SEOR       │ ────────────────► │      WIAM       │
│                 │                    │ (Resource Server)│                   │    (Backend)    │
└─────────────────┘                    └─────────────────┘                    └─────────────────┘
         │                                      │
         │ Login/Token Request                  │ Validate JWT
         ▼                                      ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                              AUTHSERVER (Authorization Server)                               │
│                                      Porta: 9000                                            │
└─────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🔓 API ESCLUSE DALLA SECURIZZAZIONE

Le seguenti API del SEOR devono rimanere **pubbliche** (non protette da JWT):

1. **POST** `/api/v1/utente/registra-utente` - Registrazione nuovo utente
2. **POST** `/api/v1/utente/recupero-password` - Recupero password

---

## 📝 STEP 1: Configurazione Authorization Server (authserver)

### 1.1 Aggiornamento pom.xml authserver
Verificare che siano presenti le seguenti dipendenze (già presenti):
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security-oauth2-authorization-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

### 1.2 Creare la configurazione del Authorization Server
**File:** `authserver/src/main/java/com/tsm/ur/card/authserver/config/AuthorizationServerConfig.java`

Implementare:
- `RegisteredClientRepository` - Bean per registrare i client OAuth2 (es: "seor-client")
- `AuthorizationServerSettings` - Configurazione issuer URL (es: `http://localhost:9000`)
- `JWKSource<SecurityContext>` - Chiavi RSA per firmare i JWT
- `JwtDecoder` - Per decodificare i token
- `SecurityFilterChain` per authorization server endpoints (`/oauth2/authorize`, `/oauth2/token`, etc.)

### 1.3 Creare la configurazione Security
**File:** `authserver/src/main/java/com/tsm/ur/card/authserver/config/SecurityConfig.java`

Implementare:
- `SecurityFilterChain` per proteggere le risorse
- `UserDetailsService` che recupera gli utenti da MongoDB (usando la collection "utente" esistente)
- `PasswordEncoder` (BCrypt)

### 1.4 Creare UserDetailsService personalizzato
**File:** `authserver/src/main/java/com/tsm/ur/card/authserver/service/MongoUserDetailsService.java`

- Implementare `UserDetailsService`
- Recuperare l'utente dalla collection MongoDB "utente" tramite email/username
- Verificare che `enrollment` sia `true` per permettere il login
- Mappare i dati utente a `UserDetails`

### 1.5 Creare Entity Utente per authserver
**File:** `authserver/src/main/java/com/tsm/ur/card/authserver/entity/Utente.java`

```java
@Document("utente")
@Data
public class Utente {
    @MongoId
    private String email;
    private String password;
    private String username;
    private String dataRegistrazione;
    private Boolean enrollment;
    private Integer otpCount;
    private String otpLastTime;
}
```

### 1.6 Creare Repository Utente
**File:** `authserver/src/main/java/com/tsm/ur/card/authserver/repository/UtenteRepository.java`

```java
@Repository
public interface UtenteRepository extends MongoRepository<Utente, String> {
    Optional<Utente> findByUsername(String username);
    Optional<Utente> findByEmail(String email);
}
```

### 1.7 Configurare application.yaml authserver
**File:** `authserver/src/main/resources/application.yaml`

```yaml
spring:
  application:
    name: authserver
  data:
    mongodb:
      uri: mongodb://localhost:27017/tsm-ur-card-list

server:
  port: 9000

logging:
  level:
    org.springframework.security: DEBUG
```

### 1.8 Personalizzare JWT con username
**File:** `authserver/src/main/java/com/tsm/ur/card/authserver/config/JwtTokenCustomizer.java`

Implementare `OAuth2TokenCustomizer<JwtEncodingContext>` per aggiungere il claim `username` nel JWT:
- Aggiungere claim `username` con il valore username dell'utente
- Aggiungere altri claim necessari (es: email)

---

## 📝 STEP 2: Configurazione Resource Server (seor)

### 2.1 Aggiornamento pom.xml seor
Aggiungere le dipendenze OAuth2 Resource Server:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2.2 Creare la configurazione Security per Resource Server
**File:** `seor/src/main/java/com/tsm/ur/card/seor/config/SecurityConfig.java`

Implementare:
- `SecurityFilterChain` che:
  - Permette accesso pubblico a:
    - `POST /api/v1/utente/registra-utente`
    - `POST /api/v1/utente/recupero-password`
  - Richiede autenticazione JWT per tutte le altre API
- Configurare `oauth2ResourceServer()` con JWT validation

### 2.3 Aggiornare application.yaml seor
**File:** `seor/src/main/resources/application.yaml`

Aggiungere:
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9000
          jwk-set-uri: http://localhost:9000/oauth2/jwks
```

### 2.4 Creare utility per estrarre username dal JWT
**File:** `seor/src/main/java/com/tsm/ur/card/seor/util/JwtUtils.java`

```java
@Component
public class JwtUtils {
    
    public String extractUsername(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("username");
        }
        throw new IllegalStateException("Cannot extract username from JWT");
    }
    
    public String extractUsernameFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return extractUsername(authentication);
    }
}
```

---

## 📝 STEP 3: Modificare i Controller SEOR per utilizzare username dal JWT

### 3.1 Modificare i metodi che richiedono username
Per ogni endpoint che attualmente riceve `username` come parametro o nel body, modificare per estrarlo dal JWT.

**Endpoint da modificare:**

#### UtenteController.java
- `POST /enroll/{username}` → Rimuovere PathVariable, estrarre username dal JWT
- `POST /cambio-password` → Ignorare username nel request body, usare quello dal JWT

#### PokemonController.java
- `POST /addcard` → Ignorare `usernameAssociato` nel body, usare JWT username
- `DELETE /cancellacarta` → Ignorare `username` nel body, usare JWT username
- `GET /getcardsbyusername/{username}` → Rimuovere PathVariable, usare JWT username
- `GET /getcardbyUsernameandstato/{username}/{stato}` → Rimuovere `username` PathVariable, usare JWT
- `POST /addsealed` → Ignorare `username` nel body, usare JWT username
- `DELETE /cancellasealed` → Ignorare `username` nel body, usare JWT username
- `GET /getSealedByUsername/{username}` → Rimuovere PathVariable, usare JWT username
- `GET /getSealedByUsernameAndStato/{username}/{stato}` → Rimuovere `username` PathVariable, usare JWT

#### OnePieceController.java
- `POST /add-sealed` → Ignorare `username` nel body, usare JWT username
- `POST /delete-sealed` → Ignorare `username` nel body, usare JWT username
- `GET /get-sealed-by-user/{username}` → Rimuovere PathVariable, usare JWT username
- `GET /get-sealed-bystato/{username}/{stato}` → Rimuovere `username` PathVariable, usare JWT

#### RecapController.java
- `POST /getrecap` → Ignorare `username` nel body, usare JWT username

### 3.2 Esempio di modifica Controller
**Prima:**
```java
@GetMapping("/getcardsbyusername/{username}")
public ResponseEntity<List<CartaPokemon>> getCartePokemonByUsername(@PathVariable String username) {
    return ResponseEntity.ok(pokemonService.getCartePokemonByUsername(username));
}
```

**Dopo:**
```java
@GetMapping("/getcardsbyusername")
public ResponseEntity<List<CartaPokemon>> getCartePokemonByUsername(Authentication authentication) {
    String username = jwtUtils.extractUsername(authentication);
    return ResponseEntity.ok(pokemonService.getCartePokemonByUsername(username));
}
```

### 3.3 Modificare i Service SEOR
Aggiornare i metodi dei service per accettare `username` come parametro separato (non più dal DTO request) e passarlo alle chiamate verso WIAM.

---

## 📝 STEP 4: Testing e Validazione

### 4.1 Test Authorization Server
1. Avviare authserver sulla porta 9000
2. Verificare che gli endpoint OAuth2 siano accessibili:
   - `GET http://localhost:9000/.well-known/openid-configuration`
   - `GET http://localhost:9000/oauth2/jwks`
3. Testare il flusso di login e ottenimento token

### 4.2 Test Resource Server
1. Avviare seor sulla porta 8080
2. Verificare che le API pubbliche funzionino senza token:
   - `POST /api/v1/utente/registra-utente`
   - `POST /api/v1/utente/recupero-password`
3. Verificare che le API protette restituiscano 401 senza token
4. Verificare che le API protette funzionino con token JWT valido

### 4.3 Test Estrazione Username
1. Effettuare login e ottenere JWT
2. Chiamare un'API protetta con il JWT
3. Verificare che il `username` venga estratto correttamente dal token
4. Verificare che le chiamate verso WIAM contengano l'username corretto

---

## 📝 STEP 5: Documentazione API

### 5.1 Aggiornare la documentazione
Documentare:
- Come ottenere un JWT (endpoint `/oauth2/token`)
- Come includere il JWT nelle richieste (`Authorization: Bearer <token>`)
- Quali API sono pubbliche e quali protette

---

## 📋 CHECKLIST FINALE

### AuthServer
- [x] Configurazione Authorization Server creata
- [x] Configurazione Security creata
- [x] MongoUserDetailsService implementato
- [x] Entity Utente creata
- [x] Repository Utente creato
- [x] JWT Customizer implementato (aggiunge username al token)
- [x] application.yaml configurato con MongoDB e porta 9000
- [x] Endpoint login personalizzato (/api/v1/auth/login)
- [x] Endpoint JWKS (/oauth2/jwks)
- [ ] Test endpoint OAuth2 funzionanti

### SEOR
- [x] Dipendenze OAuth2 Resource Server aggiunte
- [x] SecurityConfig implementata con API pubbliche/protette
- [x] JwtUtils creata per estrarre username
- [x] UtenteController modificato
- [x] PokemonController modificato
- [x] OnePieceController modificato
- [x] RecapController modificato
- [x] Service modificati per accettare username separato
- [x] WiamIntegration aggiornata con metodi overloaded
- [x] application.yaml aggiornato con configurazione JWT
- [ ] Test API con JWT funzionanti

---

## ⚠️ NOTE IMPORTANTI

1. **NON MODIFICARE** il microservizio **WIAM** - è solo backend chiamato da SEOR
2. Le password nel database devono essere hashate con BCrypt
3. Solo gli utenti con `enrollment = true` possono effettuare login
4. L'issuer URL deve essere consistente tra authserver e seor
5. Considerare l'uso di variabili d'ambiente per URL e porte in produzione

---

## 🔐 FLUSSO AUTENTICAZIONE

```
1. Client invia credenziali a AuthServer (/oauth2/token)
2. AuthServer verifica credenziali su MongoDB
3. AuthServer genera JWT con claim "username"
4. Client usa JWT per chiamare API SEOR
5. SEOR valida JWT con JWK da AuthServer
6. SEOR estrae username dal JWT
7. SEOR chiama WIAM con username estratto
```


