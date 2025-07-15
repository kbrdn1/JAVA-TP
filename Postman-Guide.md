# M2I2 API Postman Collection Guide

This guide explains how to use the M2I2 API Postman collection to test the complete business workflow with role-based constraints.

## 📋 Overview

The M2I2 API implements strict business constraints:
- **Products MUST have an Admin (ADMIN role) + Seller (SELLER role)**
- **Products CAN have a Client (CLIENT role)**
- **Role validation is enforced at all levels**
- **DTOs prevent circular references in JSON responses**

## 🚀 Quick Setup

### 1. Import Collection and Environment

1. **Import Collection**: `JAVA TP.postman_collection.json`
2. **Import Environment**: `M2I2-Environment.postman_environment.json`
3. **Select Environment**: Choose "M2I2 Business Development" in Postman

### 2. Verify API is Running

```bash
# Ensure Docker services are running
docker-compose up -d

# Start the Spring Boot application
./mvnw spring-boot:run
```

### 3. Test API Status

Run the first request: **🏠 API Information > Get API Info**
Expected response should show API status and available endpoints.

## 📁 Collection Structure

### 🏠 API Information
- Basic API status and endpoint discovery
- Use to verify the API is running

### 🎭 Roles Management
- **Business Roles**: CLIENT, ADMIN, SELLER
- **Create roles first** before creating users
- Each role has specific permissions in the product workflow

### 👥 Users Management
- Create users with specific business roles
- Test role assignments and updates
- View user-specific product relationships

### 📦 Products Management
- **Core CRUD operations** with business constraints
- **Required parameters**: adminId + sellerId
- **Optional parameter**: clientId

### 🔍 Products by Role
- Query products by admin, seller, or client
- Test role-based product ownership

### 🏪 Business Operations
- Available products (no client assigned)
- Business metrics and summaries
- Role-based product views
- Client assignment/removal

### 🚨 Error Testing
- Test constraint violations
- Validate role enforcement
- Handle edge cases

## 🎯 Recommended Testing Workflow

### Phase 1: Setup Business Foundation

1. **Create Roles** (in order):
   ```
   🎭 Roles Management > Create CLIENT Role
   🎭 Roles Management > Create ADMIN Role  
   🎭 Roles Management > Create SELLER Role
   ```

2. **Create Users with Roles**:
   ```
   👥 Users Management > Create CLIENT User
   👥 Users Management > Create ADMIN User
   👥 Users Management > Create SELLER User
   ```

3. **Verify Setup**:
   ```
   🎭 Roles Management > Get All Roles
   👥 Users Management > Get All Users
   ```

### Phase 2: Test Product Business Rules

4. **Create Valid Products**:
   ```
   📦 Products Management > Create Product (Admin + Seller)
   📦 Products Management > Create Product (Admin + Seller + Client)
   ```

5. **Test Business Queries**:
   ```
   🔍 Products by Role > Get Products by Admin
   🔍 Products by Role > Get Products by Seller
   🔍 Products by Role > Get Products by Client
   ```

### Phase 3: Test Business Operations

6. **Business Workflows**:
   ```
   🏪 Business Operations > Get Available Products
   🏪 Business Operations > Assign Client to Product
   🏪 Business Operations > Get Business Summary
   ```

7. **Role-based Views**:
   ```
   🏪 Business Operations > Get Role-based View (CLIENT)
   🏪 Business Operations > Get Role-based View (ADMIN)
   🏪 Business Operations > Get Role-based View (SELLER)
   ```

### Phase 4: Test Error Scenarios

8. **Constraint Violations**:
   ```
   🚨 Error Testing > ❌ Missing Admin (Should Fail)
   🚨 Error Testing > ❌ Missing Seller (Should Fail)
   🚨 Error Testing > ❌ CLIENT as Admin (Should Fail)
   🚨 Error Testing > ❌ ADMIN as Seller (Should Fail)
   ```

## 🔧 Environment Variables

The environment includes these pre-configured variables:

| Variable | Value | Description |
|----------|--------|-------------|
| `base_url` | `http://localhost:8080` | API base URL |
| `client_user_id` | `1` | CLIENT role user |
| `admin_user_id` | `2` | ADMIN role user |
| `seller_user_id` | `3` | SELLER role user |
| `client_role_id` | `1` | CLIENT role ID |
| `admin_role_id` | `2` | ADMIN role ID |
| `seller_role_id` | `3` | SELLER role ID |

## 💡 Usage Tips

### 1. Variable Usage
- Use `{{variable_name}}` in requests
- Update variables in the environment as needed
- Variables automatically reference correct IDs

### 2. Request Dependencies
- **Create roles before users**
- **Create users before products**
- **Some requests depend on previous request responses**

### 3. Error Testing
- Error requests are marked with ❌
- Expected to return 4xx status codes
- Useful for validating business rules

### 4. Response Validation
- Check status codes (200, 201, 400, 404, 409)
- Validate JSON structure with DTOs
- Ensure no circular references in responses

## 📊 Expected Response Examples

### Successful Product Creation
```json
{
  "id": 1,
  "name": "Business Laptop",
  "price": 1299.99,
  "description": "Professional laptop",
  "stock": 10,
  "admin": {
    "id": 2,
    "email": "alice.admin@company.com",
    "roleName": "ADMIN"
  },
  "seller": {
    "id": 3,
    "email": "mike.seller@company.com", 
    "roleName": "SELLER"
  },
  "client": null
}
```

### Business Constraint Error
```json
{
  "timestamp": "2025-07-15T14:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Administrator must have ADMIN role"
}
```

### Available Products
```json
[
  {
    "id": 4,
    "name": "Monitor 4K",
    "price": 399.99,
    "admin": {"id": 2, "email": "alice.admin@company.com"},
    "seller": {"id": 3, "email": "mike.seller@company.com"},
    "client": null
  }
]
```

## 🔍 Troubleshooting

### Common Issues

1. **"User not found" errors**
   - Ensure users are created before products
   - Check user IDs in environment variables

2. **"Role validation failed" errors**
   - Verify users have correct roles
   - Check business constraint enforcement

3. **"Port already in use" errors**
   - Stop existing Spring Boot processes
   - Use `lsof -ti:8080 | xargs kill -9`

4. **Connection refused**
   - Ensure Docker MySQL is running
   - Verify Spring Boot application is started

### Verification Steps

1. **Check API Status**: Use "Get API Info" request
2. **Verify Database**: Access phpMyAdmin at `http://localhost:8181`
3. **Check Logs**: Monitor Spring Boot console output
4. **Test Basic CRUD**: Start with simple role/user operations

## 🎯 Testing Scenarios

### Scenario 1: Complete Product Lifecycle
1. Create CLIENT, ADMIN, SELLER users
2. Create product with admin + seller
3. Check in available products
4. Assign client to product
5. Verify product no longer available
6. Check business summary

### Scenario 2: Role-based Access
1. Create users with different roles
2. Create products with various assignments
3. Test role-based product views
4. Compare information visibility per role

### Scenario 3: Constraint Validation
1. Try creating products with wrong roles
2. Test missing required parameters
3. Validate error messages
4. Ensure proper HTTP status codes

## 📚 Additional Resources

- **API Documentation**: See `README.md` and `test-api.md`
- **Business Rules**: Detailed in `demo-business-constraints.sh`
- **Database Schema**: View in phpMyAdmin at `http://localhost:8181`
- **Performance Logs**: Check Spring Boot console for `@MeasureTime` metrics

## 🎉 Happy Testing!

This collection provides comprehensive testing of the M2I2 API's business constraints. Use it to validate the role-based product management system and ensure all business rules are properly enforced.

For questions or issues, refer to the main project documentation or check the Spring Boot application logs.