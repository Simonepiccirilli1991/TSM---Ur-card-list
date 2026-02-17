# AGENTS3.md - Implementazione Frontend per TSM UR Card List

## 🎯 Obiettivo
Questo documento descrive tutti gli step necessari per implementare un **frontend web completo** per la piattaforma TSM UR Card List, che permetta agli utenti di gestire collezioni di carte e prodotti sealed Pokemon e One Piece, con funzionalità di acquisto, vendita, dashboard analitiche e recap.

---

## ✅ IMPLEMENTAZIONE COMPLETATA

Il frontend è stato implementato con **Spring Boot + Thymeleaf** (non React come inizialmente pianificato).

### Stack Tecnologico Utilizzato
- **Spring Boot 4.0.2** con WebMVC
- **Thymeleaf** come template engine
- **Tailwind CSS** (via CDN) per styling
- **WebFlux WebClient** per chiamate API ai backend
- **Spring Security** per gestione sessioni

### Struttura Progetto Implementata

```
frontend/src/main/java/com/tsm/ur/card/frontend/
├── config/
│   ├── SecurityConfig.java          # Configurazione sicurezza
│   └── WebClientConfig.java          # WebClient per SEOR e AuthServer
├── controller/
│   ├── AuthController.java           # Login, Register, Forgot Password
│   ├── DashboardController.java      # Dashboard principale
│   ├── PokemonController.java        # CRUD carte e sealed Pokemon
│   ├── OnePieceController.java       # CRUD sealed One Piece
│   └── RecapController.java          # Recap collezione
├── model/
│   ├── BaseRecap.java
│   ├── BaseResponse.java
│   ├── CartaPokemon.java
│   ├── DashboardStats.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── RegisterRequest.java
│   ├── SealedOnePiece.java
│   ├── SealedPokemon.java
│   ├── UserSession.java
│   └── form/
│       ├── AggiungiCartaPokemonForm.java
│       ├── AggiungiSealedOnePieceForm.java
│       ├── AggiungiSealedPokemonForm.java
│       └── VendiProdottoForm.java
└── service/
    ├── AuthService.java              # Chiamate AuthServer
    ├── OnePieceService.java          # Chiamate API One Piece
    ├── PokemonService.java           # Chiamate API Pokemon
    └── RecapService.java             # Chiamate API Recap

frontend/src/main/resources/
├── application.yaml                   # Configurazione app
└── templates/
    ├── layout/
    │   └── main.html                  # Layout principale con sidebar
    ├── auth/
    │   ├── login.html
    │   ├── register.html
    │   └── forgot-password.html
    ├── dashboard/
    │   └── index.html
    ├── pokemon/
    │   ├── cards.html
    │   ├── add-card.html
    │   ├── card-detail.html
    │   ├── sealed.html
    │   ├── add-sealed.html
    │   └── sealed-detail.html
    ├── onepiece/
    │   ├── sealed.html
    │   ├── add-sealed.html
    │   └── sealed-detail.html
    └── recap/
        └── index.html
```

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                    FRONTEND                                          │
│                              (React + TypeScript)                                    │
│                                   Porta: 3000                                        │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  Pages:                                                                              │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐           │
│  │  Login  │ │Register │ │Dashboard│ │Pokemon  │ │One Piece│ │  Recap  │           │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘           │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                        │
                           JWT Token (Authorization Header)
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                   AUTHSERVER                                         │
│                          POST /api/v1/auth/login                                    │
│                          GET /oauth2/jwks                                           │
│                                   Porta: 9000                                        │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                        │
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                      SEOR                                            │
│                              (Resource Server)                                       │
│                                   Porta: 8080                                        │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  API Pubbliche (no JWT):                                                            │
│  • POST /api/v1/utente/registra-utente                                              │
│  • POST /api/v1/utente/recupero-password                                            │
│                                                                                      │
│  API Protette (JWT required):                                                        │
│  • /api/v1/utente/* (enroll, cambio-password)                                       │
│  • /api/v1/pokemon/card/* (carte e sealed)                                          │
│  • /api/v1/onepiece/* (sealed One Piece)                                            │
│  • /api/v1/recap/* (recap collezione)                                               │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                        │
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                      WIAM                                            │
│                                   (Backend)                                          │
│                                   Porta: 8081                                        │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🛠️ STACK TECNOLOGICO CONSIGLIATO

### Frontend Framework
- **React 18+** con **TypeScript**
- **Vite** come build tool
- **React Router v6** per routing

### State Management
- **React Query (TanStack Query)** per caching e data fetching
- **Zustand** o **Context API** per stato globale (auth, user)

### UI Components
- **Tailwind CSS** per styling
- **shadcn/ui** per componenti UI moderni
- **Lucide React** per icone
- **Recharts** o **Chart.js** per grafici dashboard

### Form & Validation
- **React Hook Form** per gestione form
- **Zod** per validazione schema

### HTTP Client
- **Axios** con interceptor per JWT

### Utilities
- **date-fns** per manipolazione date
- **react-hot-toast** o **sonner** per notifiche

---

## 📁 STRUTTURA PROGETTO FRONTEND

```
frontend/
├── public/
│   ├── favicon.ico
│   └── logo.png
├── src/
│   ├── api/
│   │   ├── axios.ts                    # Configurazione Axios con interceptor JWT
│   │   ├── auth.api.ts                 # API autenticazione
│   │   ├── utente.api.ts               # API utente
│   │   ├── pokemon.api.ts              # API Pokemon (carte + sealed)
│   │   ├── onepiece.api.ts             # API One Piece
│   │   └── recap.api.ts                # API Recap
│   ├── components/
│   │   ├── common/
│   │   │   ├── Button.tsx
│   │   │   ├── Input.tsx
│   │   │   ├── Modal.tsx
│   │   │   ├── Card.tsx
│   │   │   ├── Table.tsx
│   │   │   ├── Spinner.tsx
│   │   │   ├── Badge.tsx
│   │   │   └── ImageUpload.tsx
│   │   ├── layout/
│   │   │   ├── Header.tsx
│   │   │   ├── Sidebar.tsx
│   │   │   ├── Footer.tsx
│   │   │   └── MainLayout.tsx
│   │   ├── auth/
│   │   │   ├── LoginForm.tsx
│   │   │   ├── RegisterForm.tsx
│   │   │   ├── ForgotPasswordForm.tsx
│   │   │   └── ProtectedRoute.tsx
│   │   ├── pokemon/
│   │   │   ├── PokemonCardForm.tsx     # Form aggiunta/modifica carta
│   │   │   ├── PokemonCardList.tsx     # Lista carte Pokemon
│   │   │   ├── PokemonCardDetail.tsx   # Dettaglio singola carta
│   │   │   ├── PokemonCardFilters.tsx  # Filtri per stato
│   │   │   ├── PokemonSealedForm.tsx   # Form sealed Pokemon
│   │   │   ├── PokemonSealedList.tsx   # Lista sealed Pokemon
│   │   │   ├── SellPokemonModal.tsx    # Modal vendita
│   │   │   └── GradingSection.tsx      # Sezione gradazione
│   │   ├── onepiece/
│   │   │   ├── OnePieceSealedForm.tsx
│   │   │   ├── OnePieceSealedList.tsx
│   │   │   ├── OnePieceSealedDetail.tsx
│   │   │   └── SellOnePieceModal.tsx
│   │   ├── dashboard/
│   │   │   ├── StatsCards.tsx          # Card con statistiche
│   │   │   ├── ProfitChart.tsx         # Grafico profitti
│   │   │   ├── ExpenseChart.tsx        # Grafico spese
│   │   │   ├── CollectionValue.tsx     # Valore collezione
│   │   │   ├── RecentTransactions.tsx  # Transazioni recenti
│   │   │   └── CategoryBreakdown.tsx   # Suddivisione per categoria
│   │   └── recap/
│   │       ├── RecapFilters.tsx
│   │       ├── RecapTable.tsx
│   │       └── RecapSummary.tsx
│   ├── hooks/
│   │   ├── useAuth.ts                  # Hook autenticazione
│   │   ├── usePokemonCards.ts          # Hook carte Pokemon
│   │   ├── usePokemonSealed.ts         # Hook sealed Pokemon
│   │   ├── useOnePieceSealed.ts        # Hook sealed One Piece
│   │   ├── useRecap.ts                 # Hook recap
│   │   └── useDashboard.ts             # Hook dati dashboard
│   ├── pages/
│   │   ├── auth/
│   │   │   ├── LoginPage.tsx
│   │   │   ├── RegisterPage.tsx
│   │   │   └── ForgotPasswordPage.tsx
│   │   ├── dashboard/
│   │   │   └── DashboardPage.tsx
│   │   ├── pokemon/
│   │   │   ├── PokemonCardsPage.tsx
│   │   │   ├── PokemonSealedPage.tsx
│   │   │   ├── AddPokemonCardPage.tsx
│   │   │   └── PokemonCardDetailPage.tsx
│   │   ├── onepiece/
│   │   │   ├── OnePieceSealedPage.tsx
│   │   │   ├── AddOnePieceSealedPage.tsx
│   │   │   └── OnePieceSealedDetailPage.tsx
│   │   ├── recap/
│   │   │   └── RecapPage.tsx
│   │   ├── profile/
│   │   │   ├── ProfilePage.tsx
│   │   │   └── ChangePasswordPage.tsx
│   │   └── NotFoundPage.tsx
│   ├── store/
│   │   ├── authStore.ts                # Store autenticazione (Zustand)
│   │   └── uiStore.ts                  # Store UI (sidebar, theme)
│   ├── types/
│   │   ├── auth.types.ts
│   │   ├── pokemon.types.ts
│   │   ├── onepiece.types.ts
│   │   ├── recap.types.ts
│   │   └── common.types.ts
│   ├── utils/
│   │   ├── formatters.ts               # Formattazione date, valute
│   │   ├── validators.ts               # Validazioni custom
│   │   ├── constants.ts                # Costanti (stati, lingue, etc.)
│   │   └── storage.ts                  # LocalStorage utilities
│   ├── App.tsx
│   ├── main.tsx
│   └── index.css
├── .env
├── .env.example
├── package.json
├── tsconfig.json
├── tailwind.config.js
├── vite.config.ts
└── README.md
```

---

## 📝 STEP 1: Setup Progetto

### 1.1 Inizializzazione Progetto
```bash
npm create vite@latest frontend -- --template react-ts
cd frontend
npm install
```

### 1.2 Installazione Dipendenze
```bash
# UI & Styling
npm install tailwindcss postcss autoprefixer
npm install @radix-ui/react-dialog @radix-ui/react-dropdown-menu
npm install class-variance-authority clsx tailwind-merge
npm install lucide-react

# Routing
npm install react-router-dom

# State & Data Fetching
npm install @tanstack/react-query
npm install zustand
npm install axios

# Forms & Validation
npm install react-hook-form @hookform/resolvers zod

# Charts
npm install recharts

# Utilities
npm install date-fns
npm install sonner

# Dev dependencies
npm install -D @types/node
```

### 1.3 Configurazione Tailwind CSS
**File:** `tailwind.config.js`
```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
        },
        pokemon: {
          yellow: '#FFCB05',
          blue: '#3B4CCA',
          red: '#FF0000',
        },
        onepiece: {
          red: '#E60012',
          gold: '#FFD700',
        }
      }
    },
  },
  plugins: [],
}
```

### 1.4 Configurazione Variabili Ambiente
**File:** `.env`
```env
VITE_AUTH_SERVER_URL=http://localhost:9000
VITE_SEOR_API_URL=http://localhost:8080
```

---

## 📝 STEP 2: Implementazione Layer API

### 2.1 Configurazione Axios con JWT Interceptor
**File:** `src/api/axios.ts`

```typescript
import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_SEOR_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - aggiunge JWT
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - gestisce errori 401
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default apiClient;
```

### 2.2 API Autenticazione
**File:** `src/api/auth.api.ts`

```typescript
import axios from 'axios';

const authClient = axios.create({
  baseURL: import.meta.env.VITE_AUTH_SERVER_URL,
  headers: { 'Content-Type': 'application/json' },
});

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  message: string;
  expiresIn: number;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
}

export const authApi = {
  login: (data: LoginRequest) => 
    authClient.post<LoginResponse>('/api/v1/auth/login', data),
    
  // Registrazione va su SEOR (API pubblica)
  register: (data: RegisterRequest) =>
    axios.post(`${import.meta.env.VITE_SEOR_API_URL}/api/v1/utente/registra-utente`, data),
    
  // Recupero password va su SEOR (API pubblica)
  forgotPassword: (data: { username: string; nuovaPassword: string }) =>
    axios.post(`${import.meta.env.VITE_SEOR_API_URL}/api/v1/utente/recupero-password`, data),
};
```

### 2.3 API Pokemon
**File:** `src/api/pokemon.api.ts`

```typescript
import apiClient from './axios';
import { CartaPokemon, SealedPokemon } from '../types/pokemon.types';

export interface AggiungiCartaPokemonRequest {
  nome: string;
  lingua: string;
  espansione: string;
  prezzoAcquisto: number;
  dataAcquisto: string;
  foto?: string; // base64
  gradata: boolean;
  enteGradazione?: string;
  votoGradazione?: string;
  statoCarta: string;
}

export interface AggiungiSealedPokemonRequest {
  nome: string;
  linguea: string;
  espansione: string;
  prezzoAcquisto: number;
  dataAcquisto: string;
  dataUscitaProdottoUfficiale?: string;
  foto?: string;
  acquistatoPresso?: string;
}

export const pokemonApi = {
  // Carte Pokemon
  addCard: (data: AggiungiCartaPokemonRequest) =>
    apiClient.post('/api/v1/pokemon/card/addcard', data),
    
  deleteCard: (idCarta: string) =>
    apiClient.delete('/api/v1/pokemon/card/cancellacarta', { data: { idCarta } }),
    
  getCardById: (idCarta: string) =>
    apiClient.get<CartaPokemon>(`/api/v1/pokemon/card/getcard/${idCarta}`),
    
  getMyCards: () =>
    apiClient.get<CartaPokemon[]>('/api/v1/pokemon/card/getcardsbyusername'),
    
  getMyCardsByStato: (stato: string) =>
    apiClient.get<CartaPokemon[]>(`/api/v1/pokemon/card/getcardbyUsernameandstato/${stato}`),

  // Sealed Pokemon
  addSealed: (data: AggiungiSealedPokemonRequest) =>
    apiClient.post('/api/v1/pokemon/card/addsealed', data),
    
  deleteSealed: (idSealed: string) =>
    apiClient.delete('/api/v1/pokemon/card/cancellasealed', { data: { idSealed } }),
    
  getSealedById: (idSealed: string) =>
    apiClient.get<SealedPokemon>(`/api/v1/pokemon/card/getsealedbyid/${idSealed}`),
    
  getMySealed: () =>
    apiClient.get<SealedPokemon[]>('/api/v1/pokemon/card/getSealedByUsername'),
    
  getMySealedByStato: (stato: string) =>
    apiClient.get<SealedPokemon[]>(`/api/v1/pokemon/card/getSealedByUsernameAndStato/${stato}`),
};
```

### 2.4 API One Piece
**File:** `src/api/onepiece.api.ts`

```typescript
import apiClient from './axios';
import { SealedOnePiece } from '../types/onepiece.types';

export interface AggiungiOnePieceSealedRequest {
  nome: string;
  linguea: string;
  espansione: string;
  prezzoAcquisto: number;
  dataAcquisto: string;
  dataUscitaProdottoUfficiale?: string;
  foto?: string;
  acquistatoPresso?: string;
}

export const onepieceApi = {
  addSealed: (data: AggiungiOnePieceSealedRequest) =>
    apiClient.post('/api/v1/onepiece/add-sealed', data),
    
  deleteSealed: (idSealed: string) =>
    apiClient.post('/api/v1/onepiece/delete-sealed', { idSealed }),
    
  getSealedById: (idSealed: string) =>
    apiClient.get<SealedOnePiece>(`/api/v1/onepiece/get-sealed/${idSealed}`),
    
  getMySealed: () =>
    apiClient.get<SealedOnePiece[]>('/api/v1/onepiece/get-sealed-by-user'),
    
  getMySealedByStato: (stato: string) =>
    apiClient.get<SealedOnePiece[]>(`/api/v1/onepiece/get-sealed-bystato/${stato}`),
};
```

### 2.5 API Recap
**File:** `src/api/recap.api.ts`

```typescript
import apiClient from './axios';
import { BaseRecap } from '../types/recap.types';

export const recapApi = {
  getRecap: () =>
    apiClient.get<BaseRecap[]>('/api/v1/recap/getrecap'),
};
```

---

## 📝 STEP 3: Implementazione Types/Interfaces

### 3.1 Types Pokemon
**File:** `src/types/pokemon.types.ts`

```typescript
export interface CartaPokemon {
  id: string;
  usernameAssociato: string;
  nome: string;
  lingua: string;
  espansione: string;
  prezzoAcquisto: number;
  dataAcquisto: string;
  foto?: string;
  // Sezione gradazione
  gradata: boolean;
  enteGradazione?: string;
  votoGradazione?: string;
  // Sezione stato
  statoCarta: string; // mint, near mint, excellent, good, light played
  stato: string; // in_collezione, in_vendita, venduto
  statoAcquisto: string;
  // Sezione vendita
  prezzoVendita?: number;
  costiVendita?: number;
  netto?: number;
  dataVendita?: string;
  piattaformaVendita?: string;
  note?: string;
}

export interface SealedPokemon {
  id: string;
  usernameAssociato: string;
  nome: string;
  lingua: string;
  edizione?: string;
  espansione: string;
  prezzoAcquisto: number;
  dataAcquisto: string;
  dataUscitaProdottoUfficiale?: string;
  stato: string;
  statoAcquisto: string;
  foto?: string;
  acquistatoPresso?: string;
  // Sezione vendita
  prezzoVendita?: number;
  costiVendita?: number;
  netto?: number;
  dataVendita?: string;
  piattaformaVendita?: string;
  note?: string;
}

export type StatoCarta = 'mint' | 'near_mint' | 'excellent' | 'good' | 'light_played';
export type StatoProdotto = 'in_collezione' | 'in_vendita' | 'venduto';
```

### 3.2 Types One Piece
**File:** `src/types/onepiece.types.ts`

```typescript
export interface SealedOnePiece {
  id: string;
  usernameAssociato: string;
  nome: string;
  lingua: string;
  espansione: string;
  prezzoAcquisto: number;
  dataAcquisto: string;
  dataUscitaProdottoUfficiale?: string;
  acquistatoPresso?: string;
  stato: string;
  statoAcquisto: string;
  foto?: string;
  // Sezione vendita
  prezzoVendita?: number;
  costiVendita?: number;
  netto?: number;
  dataVendita?: string;
  piattaformaVendita?: string;
  note?: string;
}
```

### 3.3 Types Recap
**File:** `src/types/recap.types.ts`

```typescript
export interface BaseRecap {
  id: string;
  usernameAssociato: string;
  nome: string;
  lingua: string;
  espansione: string;
  prezzoAcquisto: number;
  dataAcquisto: string;
  foto?: string;
  // Sezione gradazione
  gradata?: boolean;
  enteGradazione?: string;
  votoGradazione?: string;
  // Sezione stato
  statoCarta?: string;
  stato: string;
  statoAcquisto: string;
  // Sezione vendita
  prezzoVendita?: number;
  costiVendita?: number;
  netto?: number;
  dataVendita?: string;
  piattaformaVendita?: string;
  note?: string;
  // Metadata
  tipoProdotto: 'pokemon_card' | 'pokemon_sealed' | 'onepiece_sealed';
}

export interface RecapSummary {
  totaleAcquisti: number;
  totaleVendite: number;
  profittoNetto: number;
  costiTotali: number;
  numeroProdotti: number;
  prodottiVenduti: number;
  prodottiInCollezione: number;
  prodottiInVendita: number;
}
```

---

## 📝 STEP 4: Implementazione Store (State Management)

### 4.1 Auth Store
**File:** `src/store/authStore.ts`

```typescript
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface User {
  username: string;
  email?: string;
}

interface AuthState {
  user: User | null;
  accessToken: string | null;
  isAuthenticated: boolean;
  login: (token: string, user: User) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      accessToken: null,
      isAuthenticated: false,
      login: (token, user) => {
        localStorage.setItem('accessToken', token);
        set({ accessToken: token, user, isAuthenticated: true });
      },
      logout: () => {
        localStorage.removeItem('accessToken');
        set({ accessToken: null, user: null, isAuthenticated: false });
      },
    }),
    {
      name: 'auth-storage',
    }
  )
);
```

---

## 📝 STEP 5: Implementazione Pagine

### 5.1 Pagine Autenticazione

#### LoginPage.tsx
- Form con username/password
- Validazione client-side
- Chiamata a authServer per login
- Salvataggio JWT in localStorage e store
- Redirect a dashboard dopo login

#### RegisterPage.tsx
- Form con username, email, password, conferma password
- Validazione password matching
- Chiamata API pubblica registrazione
- Messaggio conferma e redirect a login

#### ForgotPasswordPage.tsx
- Form con username e nuova password
- Chiamata API pubblica recupero password
- Messaggio conferma

### 5.2 Dashboard Page

#### DashboardPage.tsx
Componenti da includere:
- **StatsCards**: 4 card con metriche principali
  - Valore totale collezione (somma prezzi acquisto prodotti in_collezione)
  - Profitto totale (somma netto prodotti venduti)
  - Prodotti in vendita (count)
  - Prodotti venduti (count)
- **ProfitChart**: Grafico linea/barre profitti mensili
- **CategoryBreakdown**: Pie chart suddivisione per categoria (Pokemon Card, Pokemon Sealed, One Piece)
- **RecentTransactions**: Tabella ultime vendite/acquisti
- **CollectionValue**: Andamento valore collezione nel tempo

### 5.3 Pagine Pokemon

#### PokemonCardsPage.tsx
- Filtri per stato (tutti, in_collezione, in_vendita, venduto)
- Filtri per stato carta (mint, near mint, etc.)
- Grid/Lista di carte con immagine, nome, espansione, prezzo
- Azioni: visualizza dettaglio, vendi, elimina
- Pulsante "Aggiungi Carta"

#### AddPokemonCardPage.tsx
Form con campi:
- Nome carta (required)
- Lingua (select: IT, EN, JP, etc.)
- Espansione (required)
- Prezzo acquisto (required)
- Data acquisto (date picker)
- Foto (upload immagine)
- È gradata? (checkbox)
  - Se sì: Ente gradazione (PSA, BGS, CGC), Voto
- Stato carta (select: mint, near mint, excellent, good, light played)

#### PokemonCardDetailPage.tsx
- Immagine grande carta
- Tutti i dettagli carta
- Se venduta: dettagli vendita
- Azioni: Modifica, Vendi, Elimina

#### PokemonSealedPage.tsx
- Simile a PokemonCardsPage ma per sealed
- Grid prodotti sealed con filtri

### 5.4 Pagine One Piece

#### OnePieceSealedPage.tsx
- Lista/Grid prodotti sealed One Piece
- Filtri per stato
- Azioni CRUD

#### AddOnePieceSealedPage.tsx
Form con campi:
- Nome prodotto
- Lingua
- Espansione
- Prezzo acquisto
- Data acquisto
- Data uscita prodotto ufficiale
- Acquistato presso
- Foto

### 5.5 Pagina Recap

#### RecapPage.tsx
- Filtri: tipo prodotto, stato, periodo
- Tabella con tutti i prodotti
- Ordinamento per colonna
- Export CSV/Excel
- Summary in fondo: totali acquisti, vendite, profitto

### 5.6 Pagine Profilo

#### ProfilePage.tsx
- Visualizzazione dati utente
- Statistiche personali

#### ChangePasswordPage.tsx
- Form cambio password
- Validazione password attuale e nuova

---

## 📝 STEP 6: Implementazione Componenti Chiave

### 6.1 ProtectedRoute Component
```typescript
// Wrappa le route che richiedono autenticazione
// Verifica presenza JWT e redirect a login se assente
```

### 6.2 SellModal Component
Modal per registrare vendita di un prodotto:
- Prezzo vendita
- Costi vendita (spedizione, commissioni)
- Data vendita
- Piattaforma (eBay, Cardmarket, Vinted, etc.)
- Note
- Calcolo automatico netto

### 6.3 ImageUpload Component
- Upload immagine con preview
- Ridimensionamento client-side
- Conversione a base64 per invio API

### 6.4 DataTable Component
- Tabella riutilizzabile con:
  - Sorting
  - Filtering
  - Pagination
  - Selezione righe
  - Azioni per riga

---

## 📝 STEP 7: Routing

**File:** `src/App.tsx`

```typescript
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Toaster } from 'sonner';

// Pages
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import ForgotPasswordPage from './pages/auth/ForgotPasswordPage';
import DashboardPage from './pages/dashboard/DashboardPage';
import PokemonCardsPage from './pages/pokemon/PokemonCardsPage';
import AddPokemonCardPage from './pages/pokemon/AddPokemonCardPage';
import PokemonSealedPage from './pages/pokemon/PokemonSealedPage';
import OnePieceSealedPage from './pages/onepiece/OnePieceSealedPage';
import RecapPage from './pages/recap/RecapPage';
import ProfilePage from './pages/profile/ProfilePage';
import ChangePasswordPage from './pages/profile/ChangePasswordPage';

// Components
import MainLayout from './components/layout/MainLayout';
import ProtectedRoute from './components/auth/ProtectedRoute';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/forgot-password" element={<ForgotPasswordPage />} />

          {/* Protected Routes */}
          <Route element={<ProtectedRoute />}>
            <Route element={<MainLayout />}>
              <Route path="/" element={<Navigate to="/dashboard" replace />} />
              <Route path="/dashboard" element={<DashboardPage />} />
              
              {/* Pokemon */}
              <Route path="/pokemon/cards" element={<PokemonCardsPage />} />
              <Route path="/pokemon/cards/add" element={<AddPokemonCardPage />} />
              <Route path="/pokemon/cards/:id" element={<PokemonCardDetailPage />} />
              <Route path="/pokemon/sealed" element={<PokemonSealedPage />} />
              <Route path="/pokemon/sealed/add" element={<AddPokemonSealedPage />} />
              
              {/* One Piece */}
              <Route path="/onepiece/sealed" element={<OnePieceSealedPage />} />
              <Route path="/onepiece/sealed/add" element={<AddOnePieceSealedPage />} />
              <Route path="/onepiece/sealed/:id" element={<OnePieceSealedDetailPage />} />
              
              {/* Recap */}
              <Route path="/recap" element={<RecapPage />} />
              
              {/* Profile */}
              <Route path="/profile" element={<ProfilePage />} />
              <Route path="/profile/change-password" element={<ChangePasswordPage />} />
            </Route>
          </Route>

          {/* 404 */}
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </BrowserRouter>
      <Toaster position="top-right" />
    </QueryClientProvider>
  );
}

export default App;
```

---

## 📝 STEP 8: Implementazione Sidebar Navigation

**File:** `src/components/layout/Sidebar.tsx`

Menu items:
```
📊 Dashboard
📦 Pokemon
   ├── 🃏 Carte
   └── 📦 Sealed
🏴‍☠️ One Piece
   └── 📦 Sealed
📋 Recap
👤 Profilo
   ├── 👤 Il Mio Profilo
   └── 🔐 Cambia Password
🚪 Logout
```

---

## 📝 STEP 9: Grafici Dashboard

### 9.1 ProfitChart (Profitti Mensili)
- Grafico a barre o linea
- Asse X: mesi ultimi 12 mesi
- Asse Y: profitto netto
- Dati: somma netto per mese di vendita

### 9.2 CategoryBreakdown (Pie Chart)
- Pokemon Cards: % e valore
- Pokemon Sealed: % e valore
- One Piece Sealed: % e valore

### 9.3 CollectionValueTrend
- Grafico linea andamento valore nel tempo
- Basato su date acquisto

---

## 📝 STEP 10: Costanti e Utilità

### 10.1 Costanti
**File:** `src/utils/constants.ts`

```typescript
export const STATI_CARTA = [
  { value: 'mint', label: 'Mint' },
  { value: 'near_mint', label: 'Near Mint' },
  { value: 'excellent', label: 'Excellent' },
  { value: 'good', label: 'Good' },
  { value: 'light_played', label: 'Light Played' },
];

export const STATI_PRODOTTO = [
  { value: 'in_collezione', label: 'In Collezione' },
  { value: 'in_vendita', label: 'In Vendita' },
  { value: 'venduto', label: 'Venduto' },
];

export const LINGUE = [
  { value: 'IT', label: 'Italiano' },
  { value: 'EN', label: 'Inglese' },
  { value: 'JP', label: 'Giapponese' },
  { value: 'FR', label: 'Francese' },
  { value: 'DE', label: 'Tedesco' },
  { value: 'ES', label: 'Spagnolo' },
  { value: 'KR', label: 'Coreano' },
  { value: 'CN', label: 'Cinese' },
];

export const ENTI_GRADAZIONE = [
  { value: 'PSA', label: 'PSA' },
  { value: 'BGS', label: 'BGS (Beckett)' },
  { value: 'CGC', label: 'CGC' },
  { value: 'ACE', label: 'ACE' },
];

export const PIATTAFORME_VENDITA = [
  { value: 'ebay', label: 'eBay' },
  { value: 'cardmarket', label: 'Cardmarket' },
  { value: 'vinted', label: 'Vinted' },
  { value: 'subito', label: 'Subito.it' },
  { value: 'facebook', label: 'Facebook Marketplace' },
  { value: 'privato', label: 'Vendita Privata' },
  { value: 'altro', label: 'Altro' },
];
```

### 10.2 Formattatori
**File:** `src/utils/formatters.ts`

```typescript
import { format, parseISO } from 'date-fns';
import { it } from 'date-fns/locale';

export const formatCurrency = (value: number): string => {
  return new Intl.NumberFormat('it-IT', {
    style: 'currency',
    currency: 'EUR',
  }).format(value);
};

export const formatDate = (dateString: string): string => {
  return format(parseISO(dateString), 'dd/MM/yyyy', { locale: it });
};

export const formatDateTime = (dateString: string): string => {
  return format(parseISO(dateString), 'dd/MM/yyyy HH:mm', { locale: it });
};

export const calculateProfit = (
  prezzoVendita: number,
  costiVendita: number,
  prezzoAcquisto: number
): number => {
  return prezzoVendita - costiVendita - prezzoAcquisto;
};
```

---

## 📝 STEP 11: Testing

### 11.1 Unit Tests
- Test componenti con Vitest + Testing Library
- Test hooks custom
- Test utility functions

### 11.2 Integration Tests
- Test flussi completi (login -> dashboard -> CRUD)
- Test form submission
- Test gestione errori API

### 11.3 E2E Tests (opzionale)
- Playwright o Cypress
- Test scenari utente completi

---

## 📋 CHECKLIST FINALE

### Setup Progetto
- [ ] Inizializzazione Vite + React + TypeScript
- [ ] Installazione dipendenze
- [ ] Configurazione Tailwind CSS
- [ ] Configurazione variabili ambiente
- [ ] Struttura cartelle

### Layer API
- [ ] Configurazione Axios con interceptor JWT
- [ ] API autenticazione (login)
- [ ] API utente (register, recupero password, cambio password)
- [ ] API Pokemon (carte + sealed)
- [ ] API One Piece (sealed)
- [ ] API Recap

### Types
- [ ] Types autenticazione
- [ ] Types Pokemon
- [ ] Types One Piece
- [ ] Types Recap
- [ ] Types comuni

### Store
- [ ] Auth store (Zustand)
- [ ] UI store (sidebar, theme)

### Componenti Common
- [ ] Button
- [ ] Input
- [ ] Modal
- [ ] Card
- [ ] Table/DataTable
- [ ] Spinner/Loading
- [ ] Badge
- [ ] ImageUpload
- [ ] Select
- [ ] DatePicker

### Layout
- [ ] Header
- [ ] Sidebar con navigation
- [ ] MainLayout
- [ ] Footer

### Pagine Auth
- [ ] LoginPage
- [ ] RegisterPage
- [ ] ForgotPasswordPage
- [ ] ProtectedRoute

### Pagina Dashboard
- [ ] StatsCards
- [ ] ProfitChart
- [ ] CategoryBreakdown
- [ ] RecentTransactions
- [ ] CollectionValue trend

### Pagine Pokemon
- [ ] PokemonCardsPage (lista + filtri)
- [ ] AddPokemonCardPage (form)
- [ ] PokemonCardDetailPage
- [ ] EditPokemonCardPage
- [ ] SellPokemonCardModal
- [ ] PokemonSealedPage (lista + filtri)
- [ ] AddPokemonSealedPage (form)
- [ ] PokemonSealedDetailPage
- [ ] SellPokemonSealedModal

### Pagine One Piece
- [ ] OnePieceSealedPage (lista + filtri)
- [ ] AddOnePieceSealedPage (form)
- [ ] OnePieceSealedDetailPage
- [ ] SellOnePieceSealedModal

### Pagina Recap
- [ ] RecapPage con filtri
- [ ] RecapTable
- [ ] RecapSummary
- [ ] Export funzionalità

### Pagine Profilo
- [ ] ProfilePage
- [ ] ChangePasswordPage

### Funzionalità Extra
- [ ] Dark mode toggle
- [ ] Responsive design mobile
- [ ] PWA support (opzionale)
- [ ] Notifiche toast
- [ ] Gestione errori globale
- [ ] Loading states

### Testing
- [ ] Unit tests componenti
- [ ] Unit tests hooks
- [ ] Integration tests
- [ ] E2E tests (opzionale)

### Documentazione
- [ ] README.md con istruzioni setup
- [ ] Documentazione componenti (Storybook opzionale)

---

## 🔄 FLUSSI UTENTE PRINCIPALI

### Flusso 1: Registrazione e Login
```
1. Utente visita /register
2. Compila form (username, email, password)
3. Submit → API registra-utente (pubblica)
4. Redirect a /login con messaggio successo
5. Inserisce credenziali
6. Submit → API authserver/login
7. Riceve JWT, salvato in localStorage
8. Redirect a /dashboard
```

### Flusso 2: Aggiunta Carta Pokemon
```
1. Utente su /pokemon/cards
2. Click "Aggiungi Carta"
3. Redirect a /pokemon/cards/add
4. Compila form con dettagli carta
5. Upload foto (opzionale)
6. Submit → API addcard (con JWT)
7. Redirect a /pokemon/cards con notifica successo
8. Nuova carta visibile in lista
```

### Flusso 3: Vendita Prodotto
```
1. Utente su lista prodotti
2. Click "Vendi" su prodotto
3. Apre SellModal
4. Inserisce: prezzo vendita, costi, piattaforma, data
5. Sistema calcola netto automaticamente
6. Submit → API update prodotto (stato = venduto)
7. Modal chiude, lista aggiornata
8. Dashboard mostra nuovo profitto
```

### Flusso 4: Visualizzazione Dashboard
```
1. Utente su /dashboard
2. Caricamento parallelo:
   - API getMyCards
   - API getMySealed (Pokemon)
   - API getMySealed (One Piece)
   - API getRecap
3. Calcolo metriche client-side
4. Rendering grafici e card statistiche
5. Dati aggiornati ogni refresh
```

### Flusso 5: Recap e Analisi
```
1. Utente su /recap
2. Visualizza tabella tutti i prodotti
3. Applica filtri (tipo, stato, periodo)
4. Ordina per colonna (prezzo, data, profitto)
5. Visualizza summary in fondo
6. Click "Export" per download CSV
```

---

## ⚠️ NOTE IMPORTANTI

1. **JWT Management**: Il token viene salvato in localStorage e aggiunto automaticamente a tutte le richieste tramite interceptor Axios

2. **Error Handling**: Implementare gestione errori globale con toast notifications per feedback utente

3. **Loading States**: Ogni operazione API deve mostrare stato di caricamento

4. **Responsive Design**: L'applicazione deve funzionare su mobile, tablet e desktop

5. **Image Optimization**: Le foto devono essere ridimensionate client-side prima dell'upload per ridurre dimensioni

6. **Caching**: Utilizzare React Query per caching intelligente e ridurre chiamate API

7. **Validazione**: Validare tutti i form client-side con Zod prima dell'invio

8. **Sicurezza**: Non esporre mai informazioni sensibili nel frontend, il JWT contiene già username

---

## 🚀 DEPLOYMENT

### Build Production
```bash
npm run build
```

### Configurazione CORS
Assicurarsi che SEOR e AuthServer permettano CORS da:
- `http://localhost:3000` (development)
- URL produzione frontend

### Environment Variables Production
```env
VITE_AUTH_SERVER_URL=https://auth.tuodominio.com
VITE_SEOR_API_URL=https://api.tuodominio.com
```


