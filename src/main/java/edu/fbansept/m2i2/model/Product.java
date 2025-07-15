package edu.fbansept.m2i2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

  public interface add {}

  public interface update {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Integer id;

  @NotBlank(groups = { add.class, update.class })
  @Size(min = 2, max = 100, groups = { add.class, update.class })
  @Column(unique = true, nullable = false)
  protected String name;

  @DecimalMin(value = "0.01", groups = { add.class, update.class })
  @DecimalMax(value = "99999.99", groups = { add.class, update.class })
  @NotNull(groups = { add.class, update.class })
  @Column(columnDefinition = "DECIMAL(8,2)")
  protected Double price;

  @Size(max = 500, groups = { add.class, update.class })
  @Column(columnDefinition = "TEXT")
  protected String description;

  @Min(value = 0, groups = { add.class, update.class })
  @Column(nullable = false)
  protected Integer stock = 0;

  @ManyToOne
  @JoinColumn(name = "admin_id", nullable = false)
  @NotNull(groups = { add.class, update.class })
  @JsonBackReference("user-admin-products")
  protected User admin;

  @ManyToOne
  @JoinColumn(name = "seller_id", nullable = false)
  @NotNull(groups = { add.class, update.class })
  @JsonBackReference("user-seller-products")
  protected User seller;

  @ManyToOne
  @JoinColumn(name = "client_id")
  @JsonBackReference("user-client-products")
  protected User client;
}
