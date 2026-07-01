# 💈 BarberBooking Backend

Backend REST API for a barbershop booking management system, built with **Java 21**, **Spring Boot**, **Spring Security**, **JWT** and **MySQL**.

---

## 📖 About

BarberBooking Backend is a RESTful backend application developed to manage the core features of a barbershop booking platform.

The project provides APIs for user authentication, password recovery, barber service management, booking management, admin dashboard statistics and image upload for services.

The backend is fully implemented and is designed to be integrated with an Angular frontend, currently under development.

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
- Spring Boot
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

Main packages:

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

---

## ✨ Main Features

### 🔐 Authentication

- User registration
- User login
- JWT token generation
- Password encryption with BCrypt
- Role-based authorization
- Custom authentication and access denied handlers

### 🔁 Password Recovery

- Forgot password endpoint
- Reset password token generation
- Token validation
- Password reset flow
- Email sending through Mailtrap / Spring Mail

### 💈 Barber Services Management

- Create barber services
- Update barber services
- Delete barber services
- View active services
- Admin view of all services
- Enable / disable services
- Upload service images
- Automatic image deletion when needed

### 📅 Booking Management

- Create bookings
- View all bookings as admin
- View authenticated user's bookings
- Filter bookings by status
- Filter bookings by date
- Update booking status
- Delete bookings
- Prevent duplicate bookings for the same service at the same date and time

### 📊 Admin Dashboard

- Total bookings
- Pending bookings
- Confirmed bookings
- Cancelled bookings

### 🔎 Admin Search

Admin booking search supports:

- Status filter
- Username filter
- Date filter
- Pagination
- Sorting

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

---

## 🔐 Roles

The application supports two roles:

```text
USER
ADMIN
```

### USER

Can:

- View active services
- Create bookings
- View personal bookings
- Update own booking status
- Delete own bookings

### ADMIN

Can:

- Manage barber services
- View all bookings
- Search and filter bookings
- Access dashboard statistics

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

## 🧾 Validation

The project uses Jakarta Validation for request DTOs.

Examples:

- Required username and email
- Valid email format
- Strong password pattern
- Future appointment date
- Positive service price
- Positive service duration
- Required booking service ID

---

## ⚙️ Configuration

Open:

```text
src/main/resources/application.properties
```


Configure:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/barber_booking_db?createDatabaseIfNotExist=true
spring.datasource.username=<your_database_username>
spring.datasource.password=<your_database_password>

jwt.secret=<your_jwt_secret_key>
jwt.expiration=86400000

```
## Mailtrap Configuration

The **"Forgot Password"** feature uses **Mailtrap** to send password recovery emails during development.

### Setup

1. Create a free account at https://mailtrap.io/
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
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=YOUR_MAIL_USERNAME
spring.mail.password=YOUR_MAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Once configured, the application will be able to send password recovery emails through Mailtrap.
```
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

## 📚 What I Learned

During the development of this backend I strengthened my knowledge of:

- Spring Boot REST API development
- Spring Security
- JWT Authentication
- Role-based authorization
- Password reset flow
- Email integration with Spring Mail
- DTO pattern
- Layered architecture
- JPA relationships
- Spring Data Specifications
- Pagination and sorting
- File upload management
- Global exception handling
- Backend validation
- MySQL database integration

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
- Advanced admin dashboard
- Frontend Angular integration
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
The backend is completed, while the Angular frontend is currently under development.
