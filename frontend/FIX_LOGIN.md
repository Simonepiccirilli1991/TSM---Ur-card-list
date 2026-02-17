# Fix Login Frontend - TSM UR Card List

## ✅ PROBLEMA RISOLTO

Il login falliva con popup "Credenziali non valide" senza che la chiamata HTTP partisse verso l'auth server. 

### 🔍 Causa Identificata
Spring Security era configurato con `formLogin()` che intercettava la richiesta POST a `/auth/login-process` e tentava di autenticare l'utente usando un `UserDetailsService` standard che **non esisteva**. Questo causava il fallimento immediato **prima** che il controller custom venisse invocato.

In pratica:
1. L'utente inviava username/password dal form
2. Spring Security intercettava la richiesta con `formLogin()`
3. Cercava un `UserDetailsService` per validare le credenziali
4. Non trovandolo, falliva immediatamente
5. Il controller `AuthController.processLogin()` non veniva mai chiamato
6. Nessuna chiamata HTTP partiva verso l'auth server

## 🔧 Modifiche Effettuate

### 1. **SecurityConfig.java** - Rimossa autenticazione form standard
**File**: `src/main/java/com/tsm/ur/card/frontend/config/SecurityConfig.java`

✅ **RIMOSSA** la configurazione `formLogin()` di Spring Security  
✅ **AGGIUNTO** `SessionAuthenticationFilter` per verificare la sessione HTTP  
✅ **AGGIUNTO** `authenticationEntryPoint` personalizzato per reindirizzare a `/login` quando non autenticati

```java
.addFilterBefore(new SessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
.exceptionHandling(exception -> exception
    .authenticationEntryPoint((request, response, authException) -> {
        response.sendRedirect("/login");
    })
)
```

### 2. **SessionAuthenticationFilter.java** - NUOVO FILE
**File**: `src/main/java/com/tsm/ur/card/frontend/config/SessionAuthenticationFilter.java`

✅ Filtro custom che verifica la presenza di `UserSession` nella sessione HTTP  
✅ Se presente e con access token valido, crea un'autenticazione in Spring Security Context  
✅ Permette di proteggere le route senza usare form login standard di Spring Security

### 3. **AuthService.java** - Migliorato logging
**File**: `src/main/java/com/tsm/ur/card/frontend/service/AuthService.java`

✅ **Aggiunto** logging dettagliato per debugging  
✅ **Aggiunto** catch per eccezioni generiche oltre a `WebClientResponseException`  
✅ Ora logga status code e response body in caso di errore HTTP

## 🔄 Come Funziona Ora

1. **Form Submission**: L'utente compila il form di login in `/login`
2. **Controller Processing**: Il form fa POST a `/auth/login-process` (controller custom, NON intercettato da Spring Security)
3. **HTTP Call**: Il controller chiama `AuthService.login()` che fa una chiamata HTTP REST all'auth server
4. **Session Creation**: Se il login ha successo, crea una `UserSession` e la salva nella sessione HTTP
5. **Security Context**: Il `SessionAuthenticationFilter` legge la sessione e autentica l'utente in Spring Security
6. **Access Granted**: L'utente può accedere alle pagine protette (dashboard, carte, sealed, ecc.)

## 🧪 Come Testare

### Prerequisiti
Assicurati che MongoDB sia in esecuzione:
```bash
# Verifica che MongoDB sia attivo
mongosh --eval "db.adminCommand('ping')"
```

### 1. Avvia i servizi nell'ordine:

#### Auth Server (porta 9001)
```bash
cd authserver
mvn spring-boot:run -Dmaven.repo.local=~/.m2/repository
```

#### SEOR (porta 8080)
```bash
cd seor
mvn spring-boot:run -Dmaven.repo.local=~/.m2/repository
```

#### Frontend (porta 3000)
```bash
cd frontend
mvn spring-boot:run -Dmaven.repo.local=~/.m2/repository
```

### 2. Test del Login

1. Apri il browser: `http://localhost:3000/login`
2. Inserisci le credenziali di un utente registrato
3. Clicca "Accedi"

### 3. Log da Controllare

**✅ NEL FRONTEND dovresti vedere:**
```log
INFO  - Processo login per: <username>
INFO  - Tentativo login per username: <username>
DEBUG - Invio richiesta login a auth server...
INFO  - Login response ricevuta: Success
DEBUG - Access token presente: true
INFO  - Login riuscito per: <username>
```

**✅ NELL'AUTH SERVER dovresti vedere:**
```log
INFO  - Tentativo di login per username: <username>
INFO  - Login riuscito per username: <username>
```

**❌ Se c'è un errore di connessione:**
```log
ERROR - Errore generico durante login: ...
```

**❌ Se credenziali errate:**
```log
ERROR - Errore login - Status: 401 UNAUTHORIZED, Body: {...}
WARN  - Login fallito per: <username>
```

## 🎯 Cosa Verificare

### ✅ Checklist Funzionalità
- [ ] Il form di login si carica correttamente
- [ ] Le chiamate HTTP partono verso l'auth server (visibile nei log)
- [ ] Con credenziali corrette, redirect a `/dashboard`
- [ ] Con credenziali errate, messaggio di errore e resta in `/login`
- [ ] La sessione viene salvata correttamente
- [ ] Le pagine protette sono accessibili dopo il login
- [ ] Il logout funziona e invalida la sessione
- [ ] Dopo logout, le pagine protette non sono più accessibili

### 🔐 Endpoint URLs
- **Frontend**: `http://localhost:3000`
- **Auth Server**: `http://localhost:9001`
- **SEOR**: `http://localhost:8080`

## 📋 File Modificati

```
frontend/
├── src/main/java/com/tsm/ur/card/frontend/
│   ├── config/
│   │   ├── SecurityConfig.java              ✏️ MODIFICATO
│   │   └── SessionAuthenticationFilter.java  ✨ NUOVO
│   └── service/
│       └── AuthService.java                  ✏️ MODIFICATO
```

## 🐛 Troubleshooting

### Problema: "Errore di connessione al server"
**Causa**: Auth server non è in esecuzione o non è raggiungibile  
**Soluzione**: Verifica che l'auth server sia avviato su porta 9001

### Problema: "Credenziali non valide" con credenziali corrette
**Causa**: L'utente potrebbe non esistere nel database  
**Soluzione**: Registra prima un nuovo utente o verifica su MongoDB

### Problema: Dopo login vengo reindirizzato a /login
**Causa**: La sessione non viene salvata correttamente  
**Soluzione**: Verifica i log del frontend per vedere se `UserSession` viene creata

### Problema: 403 Forbidden sulle pagine protette
**Causa**: Il `SessionAuthenticationFilter` non sta autenticando correttamente  
**Soluzione**: Verifica che la `UserSession` sia nella sessione HTTP con `access_token` valido

## 📝 Note Tecniche

- Il sistema ora usa **autenticazione basata su sessione HTTP** invece di form login Spring Security
- L'access token JWT viene salvato nella sessione ma NON viene usato per le chiamate (questo potrebbe essere implementato in futuro)
- CSRF è disabilitato (attenzione in produzione!)
- Le route pubbliche: `/`, `/login`, `/register`, `/forgot-password`, `/auth/**`
- Tutte le altre route richiedono autenticazione

## ✨ Prossimi Step Possibili

1. Implementare il refresh token
2. Aggiungere interceptor per includere JWT nelle chiamate verso SEOR
3. Riabilitare CSRF in produzione
4. Aggiungere rate limiting sul login
5. Implementare "Remember Me" functionality


