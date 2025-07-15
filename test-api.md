# M2I2 API Testing Guide

This guide shows how to test the M2I2 API endpoints using curl commands.

## Base URL
```
http://localhost:8080
```

## 🏠 Home Endpoint

### Get API Information
```bash
curl -X GET http://localhost:8080/
```

**Expected Response:**
```json
{
  "message": "Welcome to M2I2 API",
  "version": "1.0.0",
  "timestamp": "2025-07-15T13:56:15.096",
  "endpoints": {
    "users": "/api/users",
    "roles": "/api/roles",
    "products": "/api/products",
    "relationships": "See relationship endpoints below"
  },
  "status": "online"
}
```

## 👥 Users Endpoints

### 1. Get All Users
```bash
curl -X GET http://localhost:8080/api/users
```

### 2. Get User by ID
```bash
curl -X GET http://localhost:8080/api/users/1
```

### 3. Create New User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "testpassword"
  }'
```

### 4. Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "updated@example.com"
  }'
```

### 5. Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### 6. Create User with Role
```bash
curl -X POST http://localhost:8080/api/users?roleId=1 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user.with.role@example.com",
    "password": "password123"
  }'
```

### 7. Update User Role
```bash
curl -X PUT http://localhost:8080/api/users/1?roleId=2 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "updated@example.com"
  }'
```

### 8. Get Users by Role
```bash
curl -X GET http://localhost:8080/api/users/role/1
```

### 9. Get Products of a User
```bash
curl -X GET http://localhost:8080/api/users/1/products
```

## 🎭 Roles Endpoints

### 1. Get All Roles
```bash
curl -X GET http://localhost:8080/api/roles
```

### 2. Get Role by ID
```bash
curl -X GET http://localhost:8080/api/roles/1
```

### 3. Create New Role
```bash
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ROLE_MODERATOR"
  }'
```

### 4. Update Role
```bash
curl -X PUT http://localhost:8080/api/roles/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ROLE_UPDATED"
  }'
```

### 5. Delete Role
```bash
curl -X DELETE http://localhost:8080/api/roles/1
```

### 6. Get Users by Role ID
```bash
curl -X GET http://localhost:8080/api/roles/1/users
```

## 📦 Products Endpoints

### 1. Get All Products
```bash
curl -X GET http://localhost:8080/api/products
```

### 2. Get Product by ID
```bash
curl -X GET http://localhost:8080/api/products/1
```

### 3. Create New Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Laptop",
    "price": 999.99,
    "description": "High-performance laptop for developers",
    "stock": 10
  }'
```

### 4. Update Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Laptop",
    "price": 1199.99,
    "description": "Updated high-performance laptop",
    "stock": 5
  }'
```

### 5. Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

### 6. Create Product for Specific User
```bash
curl -X POST http://localhost:8080/api/products?userId=1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "User Specific Laptop",
    "price": 1299.99,
    "description": "Laptop assigned to specific user",
    "stock": 1
  }'
```

### 7. Update Product Owner
```bash
curl -X PUT http://localhost:8080/api/products/1?userId=2 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Transferred Laptop",
    "price": 1299.99,
    "description": "Laptop transferred to another user",
    "stock": 1
  }'
```

### 8. Get Products by User
```bash
curl -X GET http://localhost:8080/api/products/user/1
```

## 🔗 Relationship Endpoints

### Role-User Relationships
```bash
# Get all users with a specific role
curl -X GET http://localhost:8080/api/roles/1/users

# Alternative way to get users by role
curl -X GET http://localhost:8080/api/users/role/1

# Create user with specific role
curl -X POST http://localhost:8080/api/users?roleId=1 \
  -H "Content-Type: application/json" \
  -d '{"email": "new@example.com", "password": "pass123"}'

# Update user's role
curl -X PUT http://localhost:8080/api/users/1?roleId=2 \
  -H "Content-Type: application/json" \
  -d '{"email": "updated@example.com"}'
```

### User-Product Relationships
```bash
# Get all products owned by a user
curl -X GET http://localhost:8080/api/users/1/products

# Alternative way to get products by user
curl -X GET http://localhost:8080/api/products/user/1

# Create product for specific user
curl -X POST http://localhost:8080/api/products?userId=1 \
  -H "Content-Type: application/json" \
  -d '{"name": "User Product", "price": 99.99, "stock": 1}'

# Transfer product to another user
curl -X PUT http://localhost:8080/api/products/1?userId=2 \
  -H "Content-Type: application/json" \
  -d '{"name": "Transferred Product", "price": 99.99, "stock": 1}'
```

## 🔧 Other Endpoints

### Hello Endpoint
```bash
curl -X GET http://localhost:8080/hello
```

**Expected Response:**
```
Hello
```

## 📊 Test Data

The application comes with pre-loaded test data:

### Users:
- ID: 1, Email: john.doe@example.com, Password: root
- ID: 2, Email: jane.smith@example.com, Password: root
- ID: 3, Email: mike.johnson@example.com, Password: root

### Roles:
- ID: 1, Name: ROLE_USER
- ID: 2, Name: ROLE_ADMIN
- ID: 3, Name: ROLE_SUPERADMIN

### Products:
- ID: 1, Name: Laptop Dell XPS 13, Price: $1299.99, Stock: 15
- ID: 2, Name: Wireless Mouse, Price: $29.99, Stock: 50
- ID: 3, Name: Mechanical Keyboard, Price: $89.99, Stock: 25
- ID: 4, Name: Monitor 4K 27-inch, Price: $399.99, Stock: 8
- ID: 5, Name: USB-C Hub, Price: $49.99, Stock: 30
- ID: 6, Name: Webcam HD, Price: $79.99, Stock: 20
- ID: 7, Name: Smartphone Stand, Price: $15.99, Stock: 100
- ID: 8, Name: Bluetooth Headphones, Price: $149.99, Stock: 12

## 🚨 Error Handling

### Validation Errors (400 Bad Request)
When creating a user with invalid data:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "invalid-email",
    "password": ""
  }'
```

**Expected Response:**
```json
{
  "email": "Email is malformed",
  "password": "must not be blank"
}
```

When creating a product with invalid data:
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "price": -10.99,
    "stock": -5
  }'
```

**Expected Response:**
```json
{
  "name": "must not be blank",
  "price": "must be greater than or equal to 0.01",
  "stock": "must be greater than or equal to 0"
}
```

### Not Found (404)
When trying to access non-existent resource:
```bash
curl -X GET http://localhost:8080/api/users/999
```

**Expected Response:**
```
HTTP 404 Not Found
```

### Conflict (409)
When creating a user with existing email:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "newpassword"
  }'
```

**Expected Response:**
```json
{
  "message": "A data integrity constraint violation was detected"
}
```

## 📈 Performance Monitoring

The API includes performance monitoring with the `@MeasureTime` annotation. Check the application logs to see execution times:

```
⏱️  Retrieving all users executed in 15.23 milliseconds
⏱️  Retrieving user by ID with parameters: [1] executed in 8.45 milliseconds
⏱️  Creating a new user executed in 25.67 milliseconds
⏱️  Updating user with parameters: [1] executed in 12.34 milliseconds
⏱️  Deleting user with parameters: [1] executed in 18.90 milliseconds
⏱️  Retrieving all products executed in 12.56 milliseconds
⏱️  Creating a new product executed in 20.34 milliseconds
⏱️  Retrieving users by role ID with parameters: [1] executed in 8.91 milliseconds
⏱️  Retrieving products by user ID with parameters: [1] executed in 11.45 milliseconds
```

## 🐛 Debugging

To see detailed logs, you can add debug logging by setting the log level in `application.properties`:

```properties
logging.level.edu.fbansept.m2i2=DEBUG
```

## 📱 Using with Frontend

These endpoints can be used with any frontend framework. Example with JavaScript fetch:

```javascript
// Get all users
fetch('http://localhost:8080/api/users')
  .then(response => response.json())
  .then(users => console.log(users));

// Create new user
fetch('http://localhost:8080/api/users', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    email: 'newuser@example.com',
    password: 'password123'
  })
})
.then(response => response.json())
.then(user => console.log(user));

// Get all products
fetch('http://localhost:8080/api/products')
  .then(response => response.json())
  .then(products => console.log(products));

// Create new product
fetch('http://localhost:8080/api/products', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    name: 'New Product',
    price: 99.99,
    description: 'A great new product',
    stock: 20
  })
})
.then(response => response.json())
.then(product => console.log(product));

// Get users by role
fetch('http://localhost:8080/api/roles/1/users')
  .then(response => response.json())
  .then(users => console.log('Users with ROLE_USER:', users));

// Get products of a user
fetch('http://localhost:8080/api/users/1/products')
  .then(response => response.json())
  .then(products => console.log('Johns products:', products));

// Create product for specific user
fetch('http://localhost:8080/api/products?userId=1', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    name: 'User Specific Product',
    price: 299.99,
    description: 'Product assigned to specific user',
    stock: 5
  })
})
.then(response => response.json())
.then(product => console.log('Created product for user:', product));
```

## 🌐 phpMyAdmin Access

You can also view the database directly via phpMyAdmin:
- URL: http://localhost:8181
- Username: root
- Password: root

Happy testing! 🚀