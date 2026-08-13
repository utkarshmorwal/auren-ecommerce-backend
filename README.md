# Auren E-Commerce Backend

Auren is a full-stack e-commerce application backend built with Java and Spring Boot. It provides REST APIs for authentication, product management, orders, reviews, payments, OTP-based verification, and role-based administration.

## 🚀 Live Project

**Frontend:** [https://auren-ecommerce-frontend.vercel.app/](https://auren-clothing-fashion.vercel.app/)

**Backend:** Deployed on Railway

## 🛠️ Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL
- REST APIs
- Maven
- Razorpay
- Brevo
- Git & GitHub

## ✨ Features

### Authentication & Authorization

- User registration and login
- JWT-based authentication
- OTP verification
- Role-based authorization
- Admin and customer roles
- Protected REST endpoints

### Product Management

- Create products
- Update products
- Delete products
- Product listing
- Product search/filtering
- Product images
- Stock management
- Product categories
- Product specifications

### Orders

- Create orders
- View orders
- Order status management
- Admin order management

### Reviews

- Product reviews
- Review management
- Rating system

### Payments

- Razorpay payment integration
- Payment verification

### Email

- OTP/email functionality using Brevo
- Gmail SMTP configuration

## 🔐 Security

The application uses Spring Security and JWT authentication.

Sensitive credentials are configured using environment variables rather than being committed to GitHub.

Example:

```properties
DB_URL=
DB_USERNAME=
DB_PASSWORD=

JWT_SECRET=

RAZORPAY_KEY_ID=
RAZORPAY_KEY_SECRET=

BREVO_API_KEY=
BREVO_SENDER_EMAIL=
```

## ⚙️ Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/auren-ecommerce-backend.git
cd auren-ecommerce-backend
```

### 2. Configure MySQL

Create a MySQL database:

```sql
CREATE DATABASE auren;
```

Configure the required environment variables.

### 3. Run the application

```bash
mvn spring-boot:run
```

The backend will run at:

```text
http://localhost:8080
```

## 🌐 Deployment

The backend is deployed using Railway.

The frontend communicates with the backend through the deployed REST API.

## 📂 Project Structure

```text
src/main/java/com/ecommerce
│
├── controller
├── model
├── repository
├── service
├── security
└── EcommerceBackendApplication.java
```

## 👨‍💻 Author

**UTKARSH MORWAL**

Java Full-Stack Developer
