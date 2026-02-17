# Fix JWKSource NullPointerException - Auth Server

## ✅ PROBLEMA RISOLTO

### 🔍 Errore Originale
```
ERROR - Errore durante il login: Cannot invoke "com.nimbusds.jose.jwk.JWKSelector.select(com.nimbusds.jose.jwk.JWKSet)" because "jwkSelector" is null
```

### 🎯 Causa Root
Nel metodo `generateToken()` dell'`AuthController`, veniva chiamato:
```java
var jwkList = jwkSource.get(null, null);  // ❌ ERRORE: primo parametro null!
```

Il metodo `JWKSource.get()` richiede un `JWKSelector` come primo parametro per selezionare le chiavi appropriate dal set. Passare `null` causava un `NullPointerException` interno.

## 🔧 Soluzione Implementata

**File modificato**: `authserver/src/main/java/com/tsm/ur/card/authserver/controller/AuthController.java`

### Modifiche Effettuate

1. **Aggiunti import necessari**:
   ```java
   import com.nimbusds.jose.jwk.JWKMatcher;
   import com.nimbusds.jose.jwk.JWKSelector;
   ```

2. **Modificato il metodo `generateToken()`**:
   ```java
   private String generateToken(String username) throws Exception {
       // Crea un JWKSelector per selezionare chiavi RSA
       JWKMatcher jwkMatcher = new JWKMatcher.Builder()
               .keyType(com.nimbusds.jose.jwk.KeyType.RSA)
               .build();
       JWKSelector jwkSelector = new JWKSelector(jwkMatcher);
       
       // Ottieni la chiave RSA dal JWKSource
       var jwkList = jwkSource.get(jwkSelector, null);  // ✅ CORRETTO!
       if (jwkList.isEmpty()) {
           throw new IllegalStateException("Nessuna chiave RSA trovata nel JWKSource");
       }
       RSAKey rsaKey = (RSAKey) jwkList.get(0);
       
       // ...resto del codice per generare il JWT
   }
   ```

### Cosa è Cambiato

#### Prima (❌)
```java
// ERRORE: jwkSelector è null
var jwkList = jwkSource.get(null, null);
RSAKey rsaKey = (RSAKey) jwkList.getFirst();
```

#### Dopo (✅)
```java
// CORRETTO: crea un JWKSelector valido
JWKMatcher jwkMatcher = new JWKMatcher.Builder()
        .keyType(com.nimbusds.jose.jwk.KeyType.RSA)
        .build();
JWKSelector jwkSelector = new JWKSelector(jwkMatcher);

var jwkList = jwkSource.get(jwkSelector, null);
if (jwkList.isEmpty()) {
    throw new IllegalStateException("Nessuna chiave RSA trovata nel JWKSource");
}
RSAKey rsaKey = (RSAKey) jwkList.get(0);
```

## 🔄 Come Funziona

1. **JWKMatcher**: Definisce i criteri di selezione (tipo di chiave = RSA)
2. **JWKSelector**: Wrapper del matcher usato per interrogare il JWKSource
3. **JWKSource.get()**: Restituisce le chiavi che matchano i criteri
4. **Validazione**: Verifica che almeno una chiave sia stata trovata
5. **Estrazione**: Prende la prima chiave RSA dalla lista

## 🧪 Test

### Come Testare il Fix

```bash
# 1. Riavvia l'Auth Server
cd authserver
mvn spring-boot:run -Dmaven.repo.local=~/.m2/repository

# 2. Prova il login
POST http://localhost:9001/api/v1/auth/login
{
  "username": "sim12",
  "password": "password123"
}
```

### Log Attesi

**Prima del fix (❌):**
```
INFO  - Tentativo di login per username: sim12
ERROR - Errore durante il login: Cannot invoke "com.nimbusds.jose.jwk.JWKSelector.select(...)" because "jwkSelector" is null
```

**Dopo il fix (✅):**
```
INFO  - Tentativo di login per username: sim12
INFO  - Login riuscito per username: sim12
```

### Response Attesa

```json
{
  "accessToken": "eyJraWQiOiI...[JWT completo]...",
  "tokenType": "Bearer",
  "message": "Login effettuato con successo",
  "expiresIn": 3600
}
```

## 📋 Dettagli Tecnici

### JWKSelector Spiegato

Il `JWKSelector` è un componente di Nimbus JOSE+JWT che:
- Filtra le chiavi in un `JWKSet` in base a criteri specifici
- Supporta vari filtri: tipo di chiave, algoritmo, uso, key ID, etc.
- È **obbligatorio** quando si interroga un `JWKSource`

### Perché Serve

Nel nostro caso:
- L'Auth Server ha un `JWKSet` con chiavi RSA
- Dobbiamo estrarre la chiave RSA per firmare il JWT
- Il `JWKSource.get()` richiede un selector per sapere quale chiave restituire
- Senza selector (null), il metodo non può funzionare

### Alternativa

Se avessimo una sola chiave e non volessimo usare il selector, potremmo:
```java
// Alternativa: iniettare direttamente il JWKSet
@Bean
public JWKSet jwkSet() {
    // ...genera chiave...
    return new JWKSet(rsaKey);
}

// Nel controller
private final JWKSet jwkSet;

private String generateToken(String username) {
    RSAKey rsaKey = (RSAKey) jwkSet.getKeys().get(0);
    // ...resto del codice
}
```

Ma l'approccio con `JWKSelector` è più robusto e standard.

## 🔐 Sicurezza

Questa modifica **non compromette la sicurezza**:

✅ La chiave RSA viene ancora estratta dal JWKSource sicuro  
✅ Il JWT viene firmato correttamente con RS256  
✅ La chiave privata resta protetta e non esposta  
✅ Il processo di firma non è cambiato, solo il modo di recuperare la chiave  

## 📊 Stack Trace Completo (Prima del Fix)

```
java.lang.NullPointerException: Cannot invoke "com.nimbusds.jose.jwk.JWKSelector.select(com.nimbusds.jose.jwk.JWKSet)" because "jwkSelector" is null
    at com.nimbusds.jose.jwk.source.ImmutableJWKSet.get(ImmutableJWKSet.java:87)
    at com.tsm.ur.card.authserver.controller.AuthController.generateToken(AuthController.java:78)
    at com.tsm.ur.card.authserver.controller.AuthController.login(AuthController.java:57)
    ...
```

## 📝 File Modificati

```
authserver/
└── src/main/java/com/tsm/ur/card/authserver/
    └── controller/
        └── AuthController.java  ✏️ MODIFICATO
```

### Linee Modificate
- **Import aggiunti**: Linee 7-8 (JWKMatcher, JWKSelector)
- **Metodo modificato**: Linee 77-89 (generateToken)

## ✨ Benefici

1. ✅ **Robusto**: Usa le API Nimbus JOSE+JWT nel modo corretto
2. ✅ **Manutenibile**: Codice più leggibile e conforme agli standard
3. ✅ **Scalabile**: Supporta facilmente multiple chiavi in futuro
4. ✅ **Sicuro**: Nessuna compromissione sulla sicurezza
5. ✅ **Estendibile**: Facile aggiungere filtri aggiuntivi se necessario

## 🎯 Conclusione

Il problema è stato risolto creando correttamente un `JWKSelector` prima di interrogare il `JWKSource`. L'auth server può ora generare JWT correttamente durante il login.

---

**Status**: ✅ COMPLETATO  
**Testing**: Pronto per test funzionale  
**Impact**: Fix critico - Il login ora genera JWT correttamente

