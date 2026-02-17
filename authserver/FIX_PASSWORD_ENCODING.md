# Fix Password Encoding - Auth Server

## ✅ PROBLEMA RISOLTO

### 🔍 Errore Originale
```
WARN - Encoded password does not look like BCrypt
WARN - Password errata per username: gigino12
```

### 🎯 Causa Root
L'Auth Server usava **BCryptPasswordEncoder** che si aspetta password hashate in formato BCrypt, ma nel database le password sono salvate **in chiaro** dal WIAM durante la registrazione.

**Flusso problematico:**
1. WIAM salva password in chiaro: `"password123"` → DB: `"password123"`
2. Auth Server tenta login con BCrypt
3. BCrypt si aspetta: `$2a$10$...` ma trova `"password123"`
4. Match fallisce → Login negato

## 🔧 Soluzione Implementata

**File modificato**: `authserver/src/main/java/com/tsm/ur/card/authserver/config/AuthorizationServerConfig.java`

### Cambiamento
Sostituito `BCryptPasswordEncoder` con **`DelegatingPasswordEncoder`** che supporta:

✅ **Password in chiaro** (NoOpPasswordEncoder) - DEFAULT  
✅ **Password BCrypt** (con prefisso `{bcrypt}`)  
✅ **Migrazione graduale** possibile in futuro

### Codice Implementato
```java
@Bean
public PasswordEncoder passwordEncoder() {
    // Supporta sia password in chiaro che BCrypt
    // Le password in chiaro non richiedono prefisso (default)
    // Le password BCrypt devono avere prefisso {bcrypt}
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("bcrypt", new BCryptPasswordEncoder());
    encoders.put("noop", NoOpPasswordEncoder.getInstance());
    
    // Usa NoOp come default per password senza prefisso (password in chiaro)
    DelegatingPasswordEncoder delegatingPasswordEncoder = 
        new DelegatingPasswordEncoder("noop", encoders);
    
    // Permetti password senza prefisso (password in chiaro)
    delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
    
    return delegatingPasswordEncoder;
}
```

## 🔄 Come Funziona Ora

### Password in Chiaro (Attuali)
```
DB: "password123"
Login con: "password123"
Encoder: NoOpPasswordEncoder
Match: ✅ SUCCESS - Confronto diretto
```

### Password BCrypt (Future)
```
DB: "{bcrypt}$2a$10$..."
Login con: "password123"
Encoder: BCryptPasswordEncoder  
Match: ✅ SUCCESS - Hash verificato
```

## ⚠️ IMPORTANTE: Miglioramenti Futuri

### Problema Attuale in WIAM
Il WIAM salva le password **in chiaro** durante la registrazione:

**File**: `wiam/src/main/java/com/tsm/ur/card/wiam/service/utenti/RegistraUtenteService.java`  
**Linea 43**: 
```java
entity.setPassword(registraUtenteRequest.password()); // ❌ Password in chiaro!
```

### ⚠️ RACCOMANDAZIONE PRODUZIONE
Prima di andare in produzione, **DEVI** implementare l'hashing nel WIAM:

#### Opzione 1: Hash nel WIAM (Consigliato)
```java
// Nel WIAM RegistraUtenteService
@RequiredArgsConstructor
public class RegistraUtenteService {
    private final PasswordEncoder passwordEncoder; // Inietta BCrypt
    
    public BaseResponse registraUtente(RegistraUtenteRequest request) {
        var entity = new Utente();
        entity.setEmail(request.email());
        entity.setPassword(passwordEncoder.encode(request.password())); // ✅ Hash BCrypt
        entity.setUsername(request.username());
        // ...resto del codice
    }
}
```

#### Opzione 2: Migrare Password Esistenti
Script per hashare password esistenti in DB:
```java
// Script di migrazione una tantum
public void migratePasswords() {
    List<Utente> utenti = utenteRepo.findAll();
    for (Utente u : utenti) {
        if (!u.getPassword().startsWith("{bcrypt}")) {
            String hashedPsw = "{bcrypt}" + passwordEncoder.encode(u.getPassword());
            u.setPassword(hashedPsw);
            utenteRepo.save(u);
        }
    }
}
```

## 🧪 Test

### Come Testare il Fix Attuale

```bash
# 1. Riavvia l'auth server
cd authserver
mvn spring-boot:run -Dmaven.repo.local=~/.m2/repository

# 2. Tenta login con utente esistente (password in chiaro)
POST http://localhost:9001/api/v1/auth/login
{
  "username": "gigino12",
  "password": "password_in_chiaro"
}
```

### Log Attesi

**Prima del fix (❌):**
```
WARN - Encoded password does not look like BCrypt
WARN - Password errata per username: gigino12
```

**Dopo il fix (✅):**
```
INFO  - Tentativo di login per username: gigino12
INFO  - Login riuscito per username: gigino12
```

## 🔐 Considerazioni di Sicurezza

### ⚠️ Attuale (Sviluppo)
- ❌ Password in chiaro nel database
- ⚠️ NoOpPasswordEncoder è **DEPRECATO** da Spring Security
- ⚠️ NON sicuro per produzione

### ✅ Futuro (Produzione)
Implementare l'hashing:
1. Aggiungere `PasswordEncoder` nel WIAM
2. Hashare password durante registrazione
3. Migrare password esistenti
4. Rimuovere NoOpPasswordEncoder dall'AuthServer
5. Usare solo BCrypt

## 📋 File Modificati

```
authserver/
└── src/main/java/com/tsm/ur/card/authserver/
    └── config/
        └── AuthorizationServerConfig.java  ✏️ MODIFICATO
```

## 📝 TODO per Produzione

- [ ] Aggiungere PasswordEncoder (BCrypt) nel WIAM
- [ ] Modificare `RegistraUtenteService.java` per hashare password
- [ ] Modificare `CambioPasswordService.java` per hashare password  
- [ ] Modificare `RecuperoPswService.java` per hashare password
- [ ] Creare script di migrazione per password esistenti
- [ ] Testare il flusso completo: registrazione → login
- [ ] Rimuovere NoOpPasswordEncoder dall'AuthServer
- [ ] Verificare security audit

## ⚡ Quick Win per Produzione

Se vuoi un fix rapido senza modificare WIAM, puoi aggiungere un prefisso alle password in chiaro esistenti nel DB:

```javascript
// Script MongoDB
db.utente.find({}).forEach(function(u) {
    if (!u.password.startsWith('{')) {
        db.utente.updateOne(
            { _id: u._id },
            { $set: { password: '{noop}' + u.password } }
        );
    }
});
```

⚠️ Questo è solo per dev/test, **NON per produzione!**

---

**Status**: ✅ LOGIN FUNZIONA (ma password non sicure)  
**Security**: ⚠️ NON PRONTO PER PRODUZIONE  
**Action Required**: Implementare hashing nel WIAM prima di produzione

