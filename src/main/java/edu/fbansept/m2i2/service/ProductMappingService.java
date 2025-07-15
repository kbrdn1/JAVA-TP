package edu.fbansept.m2i2.service;

import edu.fbansept.m2i2.dto.ProductDetailDTO;
import edu.fbansept.m2i2.model.Product;
import edu.fbansept.m2i2.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service for mapping Product entities to DTOs and vice versa
 * This service handles the conversion between database entities and data transfer objects
 * to avoid circular reference issues and control what data is exposed
 */
@Service
public class ProductMappingService {

  /**
   * Converts a Product entity to a ProductDetailDTO
   * @param product The Product entity to convert
   * @return ProductDetailDTO with all user information
   */
  public ProductDetailDTO toProductDetailDTO(Product product) {
    if (product == null) {
      return null;
    }

    ProductDetailDTO dto = new ProductDetailDTO(
      product.getId(),
      product.getName(),
      product.getPrice(),
      product.getDescription(),
      product.getStock()
    );

    // Map admin information
    if (product.getAdmin() != null) {
      dto.setAdmin(toUserSummaryDTO(product.getAdmin()));
    }

    // Map seller information
    if (product.getSeller() != null) {
      dto.setSeller(toUserSummaryDTO(product.getSeller()));
    }

    // Map client information (can be null)
    if (product.getClient() != null) {
      dto.setClient(toUserSummaryDTO(product.getClient()));
    }

    return dto;
  }

  /**
   * Converts a User entity to a UserSummaryDTO
   * @param user The User entity to convert
   * @return UserSummaryDTO with basic user information
   */
  public ProductDetailDTO.UserSummaryDTO toUserSummaryDTO(User user) {
    if (user == null) {
      return null;
    }

    String roleName = user.getRole() != null ? user.getRole().getName() : null;

    return new ProductDetailDTO.UserSummaryDTO(user.getId(), user.getEmail(), roleName);
  }

  /**
   * Converts a list of Product entities to a list of ProductDetailDTOs
   * @param products The list of Product entities to convert
   * @return List of ProductDetailDTOs
   */
  public List<ProductDetailDTO> toProductDetailDTOList(List<Product> products) {
    if (products == null) {
      return null;
    }

    return products.stream().map(this::toProductDetailDTO).collect(Collectors.toList());
  }

  /**
   * Creates a simplified product DTO without detailed user information
   * @param product The Product entity to convert
   * @return ProductDetailDTO with only basic product information
   */
  public ProductDetailDTO toSimpleProductDTO(Product product) {
    if (product == null) {
      return null;
    }

    return new ProductDetailDTO(
      product.getId(),
      product.getName(),
      product.getPrice(),
      product.getDescription(),
      product.getStock()
    );
  }

  /**
   * Creates a product DTO with only admin and seller information (no client)
   * Useful for listing products that are available for purchase
   * @param product The Product entity to convert
   * @return ProductDetailDTO with admin and seller information
   */
  public ProductDetailDTO toAvailableProductDTO(Product product) {
    if (product == null) {
      return null;
    }

    ProductDetailDTO dto = new ProductDetailDTO(
      product.getId(),
      product.getName(),
      product.getPrice(),
      product.getDescription(),
      product.getStock()
    );

    // Only include admin and seller, not client
    if (product.getAdmin() != null) {
      dto.setAdmin(toUserSummaryDTO(product.getAdmin()));
    }

    if (product.getSeller() != null) {
      dto.setSeller(toUserSummaryDTO(product.getSeller()));
    }

    // Explicitly set client to null for available products
    dto.setClient(null);

    return dto;
  }

  /**
   * Creates a product DTO with role-specific information
   * @param product The Product entity to convert
   * @param userRole The role of the requesting user
   * @return ProductDetailDTO with role-appropriate information
   */
  public ProductDetailDTO toRoleBasedProductDTO(Product product, String userRole) {
    if (product == null) {
      return null;
    }

    ProductDetailDTO dto = new ProductDetailDTO(
      product.getId(),
      product.getName(),
      product.getPrice(),
      product.getDescription(),
      product.getStock()
    );

    // Show different information based on user role
    switch (userRole) {
      case "ADMIN":
        // Admins can see all information
        return toProductDetailDTO(product);
      case "SELLER":
        // Sellers can see admin and client information
        if (product.getAdmin() != null) {
          dto.setAdmin(toUserSummaryDTO(product.getAdmin()));
        }
        if (product.getClient() != null) {
          dto.setClient(toUserSummaryDTO(product.getClient()));
        }
        break;
      case "CLIENT":
        // Clients can only see seller information
        if (product.getSeller() != null) {
          dto.setSeller(toUserSummaryDTO(product.getSeller()));
        }
        break;
      default:
        // Unknown role gets basic information only
        break;
    }

    return dto;
  }
}
