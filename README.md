# M2I2 - Spring Boot REST API

A modern Spring Boot REST API with comprehensive User, Role, and Product management, featuring strict business constraints, custom annotations, AOP-based performance monitoring, and role-based access control.

## 🎯 Project Overview

This project has been **completely converted from French to English** with REST-compliant endpoints. It provides a clean, scalable API for managing users and roles with advanced features like execution time monitoring and comprehensive error handling.

## 🔄 Major Changes Made

### Language Conversion
- ✅ **French → English**: All code, comments, and messages converted
- ✅ **Model names**: `Utilisateur` → `User`
- ✅ **Controller names**: `UtilisateurController` → `UserController`
- ✅ **DAO names**: `UtilisateurDao` → `UserDao`
- ✅ **Database tables**: `utilisateur` → `user`

### REST Endpoint Updates
- ✅ **Users**: `/api/utilisateur/liste` → `/api/users`
- ✅ **Roles**: `/api/role/liste` → `/api/roles`
- ✅ **Home**: Added new `/` endpoint with API information
- ✅ **Hello**: Moved from `/` to `/hello`

### New Features Added
- 🎯 **Custom Annotation**: `@MeasureTime` for performance monitoring
- ⚡ **AOP Aspect**: Automatic execution time logging
- 🏠 **Home Endpoint**: API information and status
- ✅ **English Validation**: All error messages in English
- 🏢 **Business Constraints**: Strict role-based product management
- 🔄 **JsonView Implementation**: Replaces @JsonBackReference/@JsonManagedReference with flexible view-based serialization
- 📊 **Hierarchical Views**: Different data visibility levels (Basic, Summary, Detail)
- 🔐 **Role-based Views**: Different data visibility per user role
- 🚀 **Performance Optimized**: Granular control over JSON payload size

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **Spring AOP**
- **MySQL 9.1**
- **Hibernate 6.6.15**
- **Lombok**
- **Jakarta Validation**
- **Docker & Docker Compose**

## 🚀 Quick Start

### Prerequisites
- Java 17 installed
- Docker and Docker Compose
- Maven (or use included wrapper)

### 1. Clone and Navigate
```bash
git clone <repository-url>
cd m2i2
```

### 2. Set Environment Variables (if needed)
Create `.env` file:
```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=m2i2_db
DB_USER=root
DB_PASSWORD=root
```

### 3. Start Database
```bash
docker-compose up -d
```

### 4. Run Application
```bash
./mvnw spring-boot:run
```

### 5. Access Services
- **API**: http://localhost:8080
- **phpMyAdmin**: http://localhost:8181 (root/root)

## 📚 API Endpoints

### 🏠 Home
- `GET /` - API information and status

### 👋 Hello  
- `GET /hello` - Simple hello message

### 👥 Users
- `GET /api/users` - Get all users (List view)
- `GET /api/users/{id}` - Get user by ID (Detail view)
- `GET /api/users/basic` - Get basic user info (id, email only)
- `GET /api/users/summary` - Get users with roles (Summary view)
- `GET /api/users/entity/{id}` - Get user entity (Summary view)
- `GET /api/users/entity/{id}/basic` - Get basic user entity
- `GET /api/users/with-role` - Get users with role information
- `GET /api/users/list-view` - Get users optimized for lists
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### 🎭 Roles
- `GET /api/roles` - Get all roles (Basic view)
- `GET /api/roles/{id}` - Get role by ID (WithUsers view)
- `POST /api/roles` - Create new role
- `PUT /api/roles/{id}` - Update role
- `DELETE /api/roles/{id}` - Delete role

### 📦 Products (with Business Constraints)
- `GET /api/products` - Get all products (List view with DTOs)
- `GET /api/products/{id}` - Get product by ID (Detail view with DTOs)
- `GET /api/products/catalog` - Get public product catalog (Basic view)
- `GET /api/products/basic` - Get basic product info (Basic view)
- `GET /api/products/with-users` - Get products with user info (Summary view)
- `GET /api/products/entity/{id}` - Get product entity (Summary view)
- `GET /api/products/entity/{id}/detail` - Get detailed product entity (Detail view)
- `POST /api/products?adminId=X&sellerId=Y&clientId=Z` - Create product with required roles
- `PUT /api/products/{id}?adminId=X&sellerId=Y&clientId=Z` - Update product relationships
- `DELETE /api/products/{id}` - Delete product
- `GET /api/products/admin/{userId}` - Get products managed by admin
- `GET /api/products/seller/{userId}` - Get products sold by seller
- `GET /api/products/client/{userId}` - Get products purchased by client
- `GET /api/products/available` - Get products without clients
- `GET /api/products/business-summary` - Get business metrics
- `GET /api/products/role-view/{userId}` - Get role-based product view
- `POST /api/products/{id}/assign-client?clientId=X` - Assign client to product
- `POST /api/products/{id}/remove-client` - Remove client from product

## 🔄 JsonView Implementation

The API uses Jackson's `@JsonView` annotation to provide flexible JSON serialization with different levels of detail and circular reference prevention.

### JsonView Hierarchy
```
JsonViews
├── Basic (base level)
├── Summary (extends Basic)
├── Detail (extends Summary)
├── User
│   ├── Basic (id, email)
│   ├── Summary (Basic + role)
│   ├── Detail (Summary + products)
│   └── List (optimized for lists)
├── Role
│   ├── Basic (id, name)
│   ├── WithUsers (Basic + users)
│   └── Detail (comprehensive)
└── Product
    ├── Basic (id, name, price, description, stock)
    ├── Summary (Basic + user info)
    ├── Detail (full product with detailed users)
    ├── List (optimized for listings)
    └── Catalog (public info only)
```

### Benefits
- **Circular Reference Prevention**: Eliminates circular reference issues
- **Granular Control**: Different endpoints return different detail levels
- **Performance**: Reduced payload size for list operations
- **Security**: Passwords automatically excluded from all responses
- **Flexibility**: Easy to add new view levels without changing entities

### Usage Examples

**Basic User Info** (`GET /api/users/basic`):
```json
[
    {
        "id": 1,
        "email": "admin@example.com"
    }
]
```

**User with Role** (`GET /api/users/summary`):
```json
[
    {
        "id": 1,
        "email": "admin@example.com",
        "role": {
            "id": 1,
            "name": "ADMIN"
        }
    }
]
```

**Product Catalog** (`GET /api/products/catalog`):
```json
[
    {
        "id": 1,
        "name": "Laptop",
        "price": 999.99,
        "description": "High-performance laptop",
        "stock": 10
    }
]
```

## 📊 Performance Monitoring

The API includes custom performance monitoring using the `@MeasureTime` annotation:

```java
@GetMapping
@MeasureTime(message = "Retrieving all users")
public List<User> getAll() {
    return userDao.findAll();
}
```

**Log Output:**
```
⏱️  Retrieving all users executed in 15.23 milliseconds
```

### Custom Annotation Features
- ✅ Configurable log levels (TRACE, DEBUG, INFO, WARN, ERROR)
- ✅ Multiple time units (NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS)
- ✅ Custom messages
- ✅ Parameter inclusion in logs
- ✅ Error handling with execution time

## 🧪 Testing

### Sample Requests

**Get API Info:**
```bash
curl http://localhost:8080/
```

**Get All Users:**
```bash
curl http://localhost:8080/api/users
```

**Create User:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "testpass"}'
```

**Get All Products:**
```bash
curl http://localhost:8080/api/products
```

**Create Product with Business Constraints:**
```bash
curl -X POST http://localhost:8080/api/products?adminId=2&sellerId=3&clientId=1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Business Laptop", "price": 999.99, "description": "Laptop with proper role assignments", "stock": 10}'
```

See `test-api.md` for complete testing guide.

## 🗄️ Database Schema

### User Table
```sql
CREATE TABLE user (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

### Role Table  
```sql
CREATE TABLE role (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);
```

### Product Table (with Business Constraints)
```sql
CREATE TABLE product (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    price DECIMAL(8,2) NOT NULL,
    description TEXT,
    stock INTEGER NOT NULL DEFAULT 0,
    admin_id INTEGER NOT NULL,
    seller_id INTEGER NOT NULL,
    client_id INTEGER,
    FOREIGN KEY (admin_id) REFERENCES user(id),
    FOREIGN KEY (seller_id) REFERENCES user(id),
    FOREIGN KEY (client_id) REFERENCES user(id)
);
```

## 📋 Test Data

The application loads test data automatically with proper business constraints:

## 🏢 Business Rules

### Product Constraints
- **REQUIRED**: Every product MUST have an administrator (ADMIN role)
- **REQUIRED**: Every product MUST have a seller (SELLER role)  
- **OPTIONAL**: Every product CAN have a client (CLIENT role)
- **VALIDATION**: Role assignments are strictly enforced
- **DTOs**: @JsonBackReference prevents circular references in JSON responses

### Role-Based Access
- **CLIENT**: Can view seller information, purchase available products
- **ADMIN**: Can view all product information, manage products
- **SELLER**: Can view admin and client information, sell products

### Business Workflows
1. Admin creates product with required seller
2. Product appears in "available" products list
3. Client can purchase product (becomes client_id)
4. Product moves to "sold" status
5. Role-based views show appropriate information

## 📊 Test Data

### Users (with Business Roles)
- john.client@example.com (ID: 1) - CLIENT role
- jane.admin@example.com (ID: 2) - ADMIN role
- mike.seller@example.com (ID: 3) - SELLER role
- sarah.client@example.com (ID: 4) - CLIENT role
- bob.admin@example.com (ID: 5) - ADMIN role

### Roles (Business Roles)
- CLIENT (ID: 1) - Can purchase products
- ADMIN (ID: 2) - Manages products, required for all products
- SELLER (ID: 3) - Sells products, required for all products

### Products (with Business Relationships)
- Laptop Dell XPS 13 (ID: 1) - $1299.99, Stock: 15, Admin: Jane, Seller: Mike, Client: John
- Wireless Mouse (ID: 2) - $29.99, Stock: 50, Admin: Jane, Seller: Mike, Client: Sarah
- Mechanical Keyboard (ID: 3) - $89.99, Stock: 25, Admin: Bob, Seller: Mike, Client: John
- Monitor 4K 27-inch (ID: 4) - $399.99, Stock: 8, Admin: Jane, Seller: Mike, Available
- USB-C Hub (ID: 5) - $49.99, Stock: 30, Admin: Bob, Seller: Mike, Client: Sarah
- Webcam HD (ID: 6) - $79.99, Stock: 20, Admin: Jane, Seller: Mike, Client: John
- Smartphone Stand (ID: 7) - $15.99, Stock: 100, Admin: Bob, Seller: Mike, Available
- Bluetooth Headphones (ID: 8) - $149.99, Stock: 12, Admin: Jane, Seller: Mike, Client: Sarah

## 🔧 Configuration

### Application Properties
```properties
# Database Configuration (uses environment variables)
spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create
spring.sql.init.mode=always
spring.sql.init.platform=test
```

### Docker Compose Services
- **MySQL Database** (port 3306)
- **phpMyAdmin** (port 8181)

## 🚨 Error Handling

### Validation Errors (400)
```json
{
  "email": "Email is malformed",
  "password": "must not be blank"
}
```

### Data Integrity Violations (409)
```json
{
  "message": "A data integrity constraint violation was detected"
}
```

### Not Found (404)
Returns HTTP 404 status for non-existent resources.

## 🏗️ Project Structure

```
src/
├── main/java/edu/fbansept/m2i2/
│   ├── annotation/          # Custom annotations
│   │   └── MeasureTime.java
│   ├── aspect/              # AOP aspects
│   │   └── MeasureTimeAspect.java
│   ├── controller/          # REST controllers
│   │   ├── HomeController.java
│   │   ├── HelloController.java
│   │   ├── UserController.java
│   │   ├── RoleController.java
│   │   └── ProductController.java
│   ├── dao/                 # Data access objects
│   │   ├── UserDao.java
│   │   ├── RoleDao.java
│   │   └── ProductDao.java
│   ├── dto/                 # Data transfer objects
│   │   └── ProductDetailDTO.java
│   ├── model/               # Entity models
│   │   ├── User.java
│   │   ├── Role.java
│   │   └── Product.java
│   ├── service/             # Business logic services
│   │   ├── ProductMappingService.java
│   │   └── ProductValidationService.java
│   ├── view/                # JsonView definitions
│   │   └── JsonViews.java
│   ├── GlobalExceptionInterceptor.java
│   └── M2i2Application.java
└── resources/
    ├── application.properties
    └── data-test.sql
```

## 🔄 Development Workflow

1. **Database Reset:** `docker-compose down -v && docker-compose up -d`
2. **Clean Build:** `./mvnw clean compile`
3. **Run Tests:** `./mvnw test`
4. **Start App:** `./mvnw spring-boot:run`
5. **View Logs:** Check console for `@MeasureTime` performance logs

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📧 Contact

For questions about this English conversion or the custom annotations implementation, please create an issue in the repository.

---

**Happy Coding! 🚀**