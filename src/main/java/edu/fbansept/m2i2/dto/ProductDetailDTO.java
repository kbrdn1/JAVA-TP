package edu.fbansept.m2i2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for Product with detailed user information
 * This DTO is used to return product information along with admin, seller, and client details
 * without causing circular reference issues in JSON serialization
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {

  private Integer id;
  private String name;
  private Double price;
  private String description;
  private Integer stock;
  private UserSummaryDTO admin;
  private UserSummaryDTO seller;
  private UserSummaryDTO client;

  /**
   * Nested DTO for user summary information
   */
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserSummaryDTO {

    private Integer id;
    private String email;
    private String roleName;

    public UserSummaryDTO(Integer id, String email) {
      this.id = id;
      this.email = email;
    }
  }

  /**
   * Constructor for creating ProductDetailDTO from Product entity
   */
  public ProductDetailDTO(
    Integer id,
    String name,
    Double price,
    String description,
    Integer stock
  ) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.description = description;
    this.stock = stock;
  }
}
