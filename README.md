# 🛍️ Veylo — Backend (Spring Boot REST API)

This is the **backend** of Veylo, a full-stack e-commerce platform. It's a Java Spring Boot REST API that powers product management, cart & order processing, payments, authentication, and the admin dashboard.

**🔗 Live API:** [veylo-ecommerce-backend.onrender.com](https://veylo-ecommerce-backend.onrender.com)
**🔗 Frontend repo:** [veylo-ecommerce-frontend](https://github.com/Anupam3792/veylo-ecommerce-frontend) · [Live site](https://veylo-ecommerce-frontend.vercel.app)

---

## ✨ What this service does

- Product catalog APIs (CRUD, search, filters, categories)
- Cart & order processing
- Secure authentication & role-based access (customer vs. admin)
- Razorpay payment order creation & signature verification
- Order management & analytics for the admin dashboard
- Review & rating APIs
- Notification system

---

## 🧱 Tech Stack

- Java, Spring Boot, Spring MVC, Spring Security
- Hibernate (JPA) — ORM & database layer
- MySQL (hosted on Aiven Cloud)
- REST APIs
- Maven
- Deployed on **Render**

---

## 🏗️ Architecture

Follows a layered **Controller → Service → Repository (DAO)** architecture with clear separation of concerns, and entity models mapped via Hibernate ORM.

```
Controller  →  Service  →  Repository  →  MySQL (Aiven Cloud)
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven
- A MySQL instance (local or cloud)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Anupam3792/veylo-ecommerce-backend.git
   cd veylo-ecommerce-backend
   ```

2. Configure your database and secrets in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://<host>:<port>/<database>
   spring.datasource.username=<username>
   spring.datasource.password=<password>

   razorpay.key.id=<your_razorpay_key_id>
   razorpay.key.secret=<your_razorpay_key_secret>

   jwt.secret=<your_jwt_secret>
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The API will start on `http://localhost:8080`.

---

## 📁 Project Structure

```
src/main/java/com/ecommerce/
├── controller/     # REST API endpoints
├── service/        # Business logic
├── repository/     # Data access layer
├── model/          # Entity classes
└── config/         # Security & app configuration
```

---

## 🔑 Key API Endpoints

| Method | Endpoint                      | Description                  |
|--------|--------------------------------|-------------------------------|
| GET    | `/api/products`                | Fetch all products            |
| GET    | `/api/products/{id}`           | Fetch a single product        |
| POST   | `/api/orders`                   | Place a new order             |
| GET    | `/api/orders`                   | Fetch orders (admin)          |
| POST   | `/api/payment/create-order`     | Create a Razorpay order       |
| POST   | `/api/payment/verify`           | Verify payment signature      |
| POST   | `/api/auth/login`               | User login                    |
| POST   | `/api/auth/register`            | User registration             |

---

## 🌐 Deployment

Deployed on [Render](https://render.com), auto-deployed from `main`. Database hosted on [Aiven Cloud](https://aiven.io) (MySQL).

---

## 🙋 Author

**Anupam Kumar**
Java Full Stack Developer
[GitHub](https://github.com/Anupam3792) • [LinkedIn](https://linkedin.com/in/anupam-kumar-4b6b94261)
