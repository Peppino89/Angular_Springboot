# Todo App Full Stack

## 📋 Overview

Todo App Full Stack è un'applicazione web sviluppata con **Angular** e **Spring Boot** che consente agli utenti di gestire le proprie attività quotidiane attraverso un sistema sicuro di autenticazione JWT.

L'applicazione implementa un sistema completo di gestione Todo (CRUD), recupero password tramite email e protezione delle API tramite Spring Security.

---

# 🚀 Tecnologie Utilizzate

## Frontend

- Angular
- TypeScript
- Reactive Forms
- Angular Router
- Route Guards
- HTTP Interceptors
- CSS3

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT Authentication
- JavaMailSender

## Database

- MySQL

## Strumenti

- Maven
- Git
- GitHub
- MailTrap (testing email)

---

# ✨ Funzionalità

## Autenticazione

- Registrazione utente
- Login
- Logout
- Password criptata con BCrypt
- JWT Authentication
- Protezione delle rotte frontend
- Protezione delle API backend

## Gestione Todo

- Creazione Todo
- Visualizzazione Todo
- Modifica Todo
- Eliminazione Todo
- Gestione stato attività
- Gestione priorità
- Gestione data di scadenza

## Recupero Password

- Forgot Password tramite email
- Generazione token UUID
- Scadenza automatica del token
- Reset Password sicuro
- Eliminazione token dopo l'utilizzo

---

# 🔐 Sicurezza

L'applicazione utilizza:

- Spring Security
- JWT (JSON Web Token)
- Password hashing con BCrypt
- Endpoint pubblici e protetti
- Validazione lato frontend e backend
- Token temporanei per il recupero password

---

# 🏗️ Architettura

Frontend (Angular)
↓
REST API
↓
Spring Boot
↓
Service Layer
↓
Repository Layer
↓
MySQL

---

# 🔄 Flusso Recupero Password

1. L'utente inserisce la propria email.
2. Il sistema verifica l'esistenza dell'account.
3. Viene generato un token UUID univoco.
4. Il token viene salvato nel database con una scadenza di 15 minuti.
5. Viene inviata una email contenente il link di reset.
6. L'utente imposta una nuova password.
7. Il token viene eliminato dal database.

---

# 📚 Principali Endpoint

## Authentication

POST /api/auth/register

POST /api/auth/login

POST /api/auth/forgot-password

POST /api/auth/reset-password

## Todo

GET /api/todos

POST /api/todos

PUT /api/todos/{id}

DELETE /api/todos/{id}

PATCH /api/todos/{id}/status

---

# 🧠 Competenze Dimostrate

Questo progetto mi ha permesso di approfondire:

- Angular Reactive Forms
- Custom Validators
- JWT Authentication
- Spring Security
- Spring Data JPA
- MySQL
- REST API Design
- Email Integration
- Transaction Management (@Transactional)
- Gestione Errori Frontend e Backend

---

# 👨‍💻 Autore

Giuseppe Giordano

Full Stack Developer

Tecnologie principali:

- Java / Spring Boot
- Angular
- React
- TypeScript
- MySQL
- MongoDB

# Configurazione del Progetto

Prima di avviare l'applicazione è necessario configurare alcuni parametri nel file:

```properties
src/main/resources/application.properties
```

Sostituire i seguenti placeholder con i propri valori:

```properties
# Database
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# JWT
jwt.secret=YOUR_JWT_SECRET

# Mailtrap
spring.mail.username=YOUR_MAIL_USERNAME
spring.mail.password=YOUR_MAIL_PASSWORD
```

---

## Configurazione Database

Creare un database MySQL e aggiornare i seguenti parametri:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todo
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

---

## Configurazione Mailtrap

La funzionalità **"Password dimenticata"** utilizza Mailtrap per l'invio delle email di recupero password.

### Passaggi

1. Registrarsi gratuitamente su https://mailtrap.io/
2. Accedere alla dashboard.
3. Aprire la sezione **Sandboxes**.
4. Creare una nuova Sandbox oppure utilizzare quella predefinita.
5. Selezionare la scheda **SMTP**.
6. Copiare i seguenti valori:
   - Host
   - Port
   - Username
   - Password

7. Inserire le credenziali nel file `application.properties`:

```properties
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=YOUR_MAIL_USERNAME
spring.mail.password=YOUR_MAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## Configurazione JWT

Generare una chiave segreta personalizzata e sostituire:

```properties
jwt.secret=YOUR_JWT_SECRET
```

Questa chiave viene utilizzata per la generazione e la validazione dei token JWT impiegati nell'autenticazione dell'applicazione.
