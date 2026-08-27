# 💈 BarberBooking Backend

Backend REST API for a barbershop booking management system, built with **Java 21**, **Spring Boot 3**, **Spring Security**, **JWT** and **MySQL**.

---

## 📖 About

BarberBooking Backend is a RESTful backend application developed to manage the core features of a barbershop booking platform.

The project provides APIs for:

- user registration and login;
- JWT-based authentication;
- role-based authorization with `USER` and `ADMIN` roles;
- password recovery through email reset tokens;
- barber service management;
- service image upload and local file storage;
- booking creation and management;
- authenticated user booking area;
- admin booking search, filtering, pagination and sorting;
- admin dashboard statistics.

The backend is designed to be integrated with an Angular frontend.

---

## 📊 Project Information

| Information       | Details                     |
| ----------------- | --------------------------- |
| **Language**      | Java 21                     |
| **Framework**     | Spring Boot 3               |
| **Security**      | Spring Security + JWT       |
| **Database**      | MySQL                       |
| **ORM**           | Spring Data JPA / Hibernate |
| **Documentation** | Swagger / OpenAPI           |
| **Email Service** | Spring Mail / Mailtrap      |
| **Architecture**  | Layered Architecture        |
| **Status**        | Backend Completed           |

---

## 🚀 Technologies

- Java 21
- Spring Boot 3
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Lombok
- Jakarta Validation
- Spring Mail
- Swagger / OpenAPI
- Spring Data JPA Specifications
- Multipart file upload
- Local file storage
- Postman

---

## 🏗 Architecture

The project follows a layered architecture:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

Main package structure:

```text
src/main/java/com/example/barberbooking
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── repository
├── service
├── specification
└── storage
```

Key authorization-related classes:

```text
SecurityConfig
├── enables method security with @EnableMethodSecurity
├── configures endpoint-level access rules
└── registers JWT authentication filter

BookingService
├── contains booking business logic
└── protects update/delete with @PreAuthorize

BookingAuthorizationService
└── checks whether a booking belongs to the authenticated user
```

---

## ✨ Main Features

### 🔐 Authentication

- User registration
- User login
- JWT token generation
- Password encryption with BCrypt
- Role included in the authentication response
- Custom authentication entry point
- Custom access denied handler
- CORS configuration for Angular frontend integration

### 🔁 Password Recovery

- Forgot password endpoint
- Password reset token generation
- Token validation endpoint
- Password reset endpoint
- Reset token expiration check
- Email sending through Spring Mail and Mailtrap

### 💈 Barber Services Management

- Public view of active services
- Admin view of all services
- Create barber services
- Update barber services
- Delete barber services
- Enable / disable services
- Upload service images
- Replace an existing service image when a new one is uploaded
- Delete associated image when a service is deleted

### 📅 Booking Management

- Create bookings as authenticated user
- View all bookings as admin
- View authenticated user's bookings
- Filter bookings by status
- Filter bookings by date
- Update booking status
- Delete bookings
- Prevent duplicate bookings for the same service at the same date and time
- Ownership-based authorization for booking update/delete operations
- Method-level security with `@PreAuthorize` on booking update/delete operations
- Booking ownership check delegated to `BookingAuthorizationService` instead of passing `@AuthenticationPrincipal` into update/delete controller methods

### 📊 Admin Dashboard

The dashboard exposes statistics for:

- total bookings;
- pending bookings;
- confirmed bookings;
- cancelled bookings.

### 🔎 Admin Booking Search

Admin booking search supports:

- status filter;
- username filter;
- date filter;
- pagination;
- sorting.

Implemented using **Spring Data JPA Specifications**.

### 🖼 Image Upload

Service images are stored locally.

Supported formats:

- `.png`
- `.jpg`
- `.jpeg`
- `.webp`

Maximum file size:

```text
2 MB
```

Uploaded files are saved under:

```text
uploads/services
```

Uploaded images are served through:

```text
/uploads/**
```

---

## 🔐 Roles and Authorization

The application supports two roles:

```text
USER
ADMIN
```

### USER

Can:

- view active services;
- create bookings;
- view personal bookings;
- update own booking status;
- delete own bookings.

### ADMIN

Can:

- manage barber services;
- upload service images;
- view all bookings;
- search and filter bookings;
- update any booking status;
- delete any booking;
- access dashboard statistics.

### Security Rules Summary

Public endpoints:

```text
/api/auth/**
GET /api/services
/uploads/**
/swagger-ui/**
/v3/api-docs/**
```

Admin-only endpoints:

```text
/api/dashboard/**
POST   /api/services/**
PUT    /api/services/**
DELETE /api/services/**
GET    /api/bookings
GET    /api/bookings/admin/**
GET    /api/bookings/status/**
GET    /api/bookings/date/**
```

Authenticated user endpoints:

```text
GET    /api/bookings/user/me
POST   /api/bookings
PATCH  /api/bookings/{id}/status
DELETE /api/bookings/{id}
```

For `PATCH /api/bookings/{id}/status` and `DELETE /api/bookings/{id}`, the service layer uses `@PreAuthorize` so that:

```text
ADMIN → can update/delete any booking
USER  → can update/delete only own bookings
```

### Booking Ownership Authorization

In the updated version, booking update and delete authorization is handled at service level with method-level security.

The `BookingService` methods use:

```java
@PreAuthorize("hasRole('ADMIN') or @bookingAuthorization.isOwner(#id, authentication.name)")
```

This means:

- `ADMIN` users can update or delete any booking;
- `USER` users can update or delete only bookings that belong to their own account;
- the authenticated username is read directly from Spring Security through `authentication.name`;
- the controller methods for update/delete do not need `@AuthenticationPrincipal` anymore;
- the ownership check is delegated to `BookingAuthorizationService.isOwner(...)`.

This keeps the controller cleaner and moves authorization rules closer to the business operation they protect.

---

## 📡 Main API Endpoints

### Auth

```http
POST /api/auth/register
POST /api/auth/login
POST /api/auth/forgot-password
GET  /api/auth/reset-password/validate?token=
POST /api/auth/reset-password
```

### Services

```http
GET    /api/services
GET    /api/services/admin
POST   /api/services
PUT    /api/services/{id}
DELETE /api/services/{id}
POST   /api/services/{id}/image
```

### Bookings

```http
GET    /api/bookings
GET    /api/bookings/admin/search
GET    /api/bookings/user/me
GET    /api/bookings/status/{status}
GET    /api/bookings/date/{date}
POST   /api/bookings
PATCH  /api/bookings/{id}/status
DELETE /api/bookings/{id}
```

### Dashboard

```http
GET /api/dashboard/stats
```

---

## 🧾 Request and Response DTOs

### AuthResponse

```json
{
  "token": "jwt-token",
  "username": "mario",
  "email": "mario@example.com",
  "role": "USER"
}
```

### BarberServiceRequest

```json
{
  "name": "Taglio Uomo",
  "description": "Taglio classico o moderno",
  "price": 18.0,
  "durationMinutes": 30,
  "active": true
}
```

### BarberServiceResponse

```json
{
  "id": 1,
  "name": "Taglio Uomo",
  "description": "Taglio classico o moderno",
  "imageUrl": "/uploads/services/image.webp",
  "price": 18.0,
  "durationMinutes": 30,
  "active": true
}
```

### BookingRequest

```json
{
  "appointmentDateTime": "2026-08-10T10:30:00",
  "barberServiceId": 1
}
```

### BookingResponse

```json
{
  "id": 1,
  "appointmentDateTime": "2026-08-10T10:30:00",
  "status": "IN_ATTESA",
  "userId": 3,
  "username": "mario",
  "barberServiceId": 1,
  "serviceName": "Taglio Uomo"
}
```

### BookingStatusRequest

```json
{
  "status": "CONFERMATA"
}
```

---

## 🧩 Enums

### Role

```text
USER
ADMIN
```

### BookingStatus

```text
IN_ATTESA
CONFERMATA
ANNULLATA
```

---

## 🔎 Admin Search Parameters

Endpoint:

```http
GET /api/bookings/admin/search
```

Supported query parameters:

| Parameter   | Description                                      | Example                  |
| ----------- | ------------------------------------------------ | ------------------------ |
| `status`    | Filter by booking status                         | `IN_ATTESA`              |
| `username`  | Filter by username                               | `mario`                  |
| `date`      | Filter by appointment date                       | `2026-08-10`             |
| `page`      | Page index                                       | `0`                      |
| `size`      | Page size                                        | `10`                     |
| `sortBy`    | Field used for sorting                           | `appointmentDateTime`    |
| `direction` | Sort direction                                   | `asc` / `desc`           |

Example:

```http
GET /api/bookings/admin/search?status=IN_ATTESA&username=mario&date=2026-08-10&page=0&size=10&sortBy=appointmentDateTime&direction=asc
```

---

## ✅ Validation

The project uses Jakarta Validation for request DTOs.

Examples:

- required username;
- valid email format;
- strong password pattern;
- required appointment date and time;
- future appointment date;
- required booking service ID;
- required barber service name and description;
- positive service price;
- positive service duration;
- required booking status when updating a booking.

---

## ⚠️ Error Handling

The project uses a global exception handler through `@RestControllerAdvice`.

Handled cases include:

- barber service not found;
- booking not found;
- user not found;
- duplicate booking;
- duplicate username;
- duplicate email;
- invalid credentials;
- invalid password;
- unauthorized booking operation;
- invalid request body;
- validation errors;
- unsupported HTTP method;
- missing resource;
- file upload errors;
- multipart file size exceeded;
- generic internal server errors.

Example error response:

```json
{
  "status": "404",
  "message": "Prenotazione non trovata"
}
```

Validation errors return a field-based map, for example:

```json
{
  "email": "Email non valida",
  "password": "La password deve contenere almeno 6 caratteri, una minuscola, una maiuscola e un carattere speciale"
}
```

---

## ⚙️ Configuration

Open:

```text
src/main/resources/application.properties
```

Configure the database:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/barber_booking_db?createDatabaseIfNotExist=true
spring.datasource.username=<your_database_username>
spring.datasource.password=<your_database_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Configure JPA/Hibernate:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

Configure JWT:

```properties
jwt.secret=<your_jwt_secret_key>
jwt.expiration=86400000
```

Configure multipart upload limits:

```properties
spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=2MB
```

---

## 📧 Mailtrap Configuration

The **Forgot Password** feature uses **Mailtrap** to send password recovery emails during development.

### Setup

1. Create a free account at Mailtrap.
2. Sign in to your Mailtrap dashboard.
3. Open the **Sandboxes** section.
4. Create a new Sandbox or use the default one.
5. Open the **SMTP** tab.
6. Copy the following credentials:
   - Host
   - Port
   - Username
   - Password
7. Configure the credentials in your `application.properties` file:

```properties
app.mail.from=noreply@barberbooking.com
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=YOUR_MAIL_USERNAME
spring.mail.password=YOUR_MAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

Once configured, the application will be able to send password recovery emails through Mailtrap.

> **Note:** Mailtrap is intended for development and testing purposes only. For production environments, configure a real SMTP provider such as Gmail, Outlook, Amazon SES, SendGrid or another email service.

---

## 🚀 Getting Started

Clone the repository:

```bash
git clone <repository-url>
```

Navigate to the backend folder:

```bash
cd barber-booking
```

Run the application:

```bash
mvn spring-boot:run
```

Or using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

The backend will run on:

```text
http://localhost:8080
```

---

## 📘 API Documentation

Swagger / OpenAPI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Suggested Manual Tests

### Auth

- Register a new user.
- Login and copy the JWT token.
- Call protected endpoints using `Authorization: Bearer <token>`.

### Services

- Call `GET /api/services` without token.
- Login as admin and create/update/delete services.
- Upload an image with `POST /api/services/{id}/image` using multipart form-data key `file`.

### Bookings

- Login as user.
- Create a booking with a future date.
- Verify it appears in `GET /api/bookings/user/me`.
- Try creating a duplicate booking for the same service at the same date and time.
- Delete only bookings owned by the authenticated user.
- Try updating/deleting another user's booking as `USER` and verify that access is denied.
- Try updating/deleting any booking as `ADMIN` and verify that the operation is allowed.

### Admin

- Login as admin.
- Call `GET /api/bookings`.
- Search bookings with `GET /api/bookings/admin/search`.
- Call `GET /api/dashboard/stats`.

---

## 📚 What I Learned

During the development of this backend I strengthened my knowledge of:

- Spring Boot REST API development
- Spring Security
- JWT Authentication
- Role-based authorization
- Method-level authorization with `@PreAuthorize`
- Ownership-based authorization using a custom authorization service
- Password reset flow
- Email integration with Spring Mail
- DTO pattern
- Layered architecture
- JPA relationships
- Spring Data Specifications
- Pagination and sorting
- File upload management
- Local file storage
- Global exception handling
- Backend validation
- MySQL database integration
- CORS configuration for Angular integration

---

## 🔮 Future Improvements

Possible future improvements include:

- Unit tests
- Integration tests
- Docker support
- Refresh token implementation
- Appointment reminders
- Email confirmation after booking
- Online payments
- Advanced admin dashboard charts
- Production-ready file storage, such as AWS S3 or Cloudinary
- Deployment

---

## 👨‍💻 Author

**Giuseppe Giordano**

Full Stack Developer

### Backend

- Java
- Spring Boot
- Node.js
- Express.js

### Frontend

- Angular
- React
- Next.js

### Databases

- MySQL
- MongoDB

---

⭐ This backend is part of the **BarberBooking Full Stack** project.
