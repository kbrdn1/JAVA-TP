package edu.fbansept.m2i2.service;

import edu.fbansept.m2i2.dao.UserDao;
import edu.fbansept.m2i2.model.Product;
import edu.fbansept.m2i2.model.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to validate Product business rules and constraints
 *
 * Business Rules:
 * - Every product MUST have an admin (ADMIN role) to manage it
 * - Every product MUST have a seller (SELLER role) to sell it
 * - Every product CAN have a client (CLIENT role) to buy it (optional)
 */
@Service
public class ProductValidationService {

  @Autowired
  private UserDao userDao;

  // Role constants
  private static final String ROLE_ADMIN = "ADMIN";
  private static final String ROLE_SELLER = "SELLER";
  private static final String ROLE_CLIENT = "CLIENT";

  /**
   * Validates a product before creation or update
   * @param product The product to validate
   * @return ValidationResult containing validation status and error message
   */
  public ValidationResult validateProduct(Product product) {
    // Check if admin is provided and valid
    if (product.getAdmin() == null) {
      return ValidationResult.error("Product must have an administrator");
    }

    Optional<User> adminUser = userDao.findById(product.getAdmin().getId());
    if (adminUser.isEmpty()) {
      return ValidationResult.error("Administrator user not found");
    }

    if (!hasRole(adminUser.get(), ROLE_ADMIN)) {
      return ValidationResult.error("Administrator must have ADMIN role");
    }

    // Check if seller is provided and valid
    if (product.getSeller() == null) {
      return ValidationResult.error("Product must have a seller");
    }

    Optional<User> sellerUser = userDao.findById(product.getSeller().getId());
    if (sellerUser.isEmpty()) {
      return ValidationResult.error("Seller user not found");
    }

    if (!hasRole(sellerUser.get(), ROLE_SELLER)) {
      return ValidationResult.error("Seller must have SELLER role");
    }

    // Check client if provided (optional)
    if (product.getClient() != null) {
      Optional<User> clientUser = userDao.findById(product.getClient().getId());
      if (clientUser.isEmpty()) {
        return ValidationResult.error("Client user not found");
      }

      if (!hasRole(clientUser.get(), ROLE_CLIENT)) {
        return ValidationResult.error("Client must have CLIENT role");
      }
    }

    return ValidationResult.success();
  }

  /**
   * Validates if a user can be assigned a specific role in product context
   * @param userId The user ID to validate
   * @param expectedRole The expected role (ADMIN, SELLER, CLIENT)
   * @return ValidationResult containing validation status and error message
   */
  public ValidationResult validateUserRole(Integer userId, String expectedRole) {
    if (userId == null) {
      return ValidationResult.error("User ID cannot be null");
    }

    Optional<User> user = userDao.findById(userId);
    if (user.isEmpty()) {
      return ValidationResult.error("User not found with ID: " + userId);
    }

    if (!hasRole(user.get(), expectedRole)) {
      return ValidationResult.error("User must have " + expectedRole + " role");
    }

    return ValidationResult.success();
  }

  /**
   * Checks if a user has a specific role
   * @param user The user to check
   * @param roleName The role name to check
   * @return true if user has the role, false otherwise
   */
  private boolean hasRole(User user, String roleName) {
    return user.getRole() != null && roleName.equals(user.getRole().getName());
  }

  /**
   * Sets the admin for a product with validation
   * @param product The product to set admin for
   * @param adminId The admin user ID
   * @return ValidationResult containing validation status and error message
   */
  public ValidationResult setProductAdmin(Product product, Integer adminId) {
    ValidationResult validation = validateUserRole(adminId, ROLE_ADMIN);
    if (!validation.isValid()) {
      return validation;
    }

    Optional<User> admin = userDao.findById(adminId);
    product.setAdmin(admin.get());
    return ValidationResult.success();
  }

  /**
   * Sets the seller for a product with validation
   * @param product The product to set seller for
   * @param sellerId The seller user ID
   * @return ValidationResult containing validation status and error message
   */
  public ValidationResult setProductSeller(Product product, Integer sellerId) {
    ValidationResult validation = validateUserRole(sellerId, ROLE_SELLER);
    if (!validation.isValid()) {
      return validation;
    }

    Optional<User> seller = userDao.findById(sellerId);
    product.setSeller(seller.get());
    return ValidationResult.success();
  }

  /**
   * Sets the client for a product with validation
   * @param product The product to set client for
   * @param clientId The client user ID (can be null)
   * @return ValidationResult containing validation status and error message
   */
  public ValidationResult setProductClient(Product product, Integer clientId) {
    if (clientId == null) {
      product.setClient(null);
      return ValidationResult.success();
    }

    ValidationResult validation = validateUserRole(clientId, ROLE_CLIENT);
    if (!validation.isValid()) {
      return validation;
    }

    Optional<User> client = userDao.findById(clientId);
    product.setClient(client.get());
    return ValidationResult.success();
  }

  /**
   * Inner class to represent validation results
   */
  public static class ValidationResult {

    private final boolean valid;
    private final String errorMessage;

    private ValidationResult(boolean valid, String errorMessage) {
      this.valid = valid;
      this.errorMessage = errorMessage;
    }

    public static ValidationResult success() {
      return new ValidationResult(true, null);
    }

    public static ValidationResult error(String message) {
      return new ValidationResult(false, message);
    }

    public boolean isValid() {
      return valid;
    }

    public String getErrorMessage() {
      return errorMessage;
    }
  }
}
