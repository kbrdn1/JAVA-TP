package edu.fbansept.m2i2.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import edu.fbansept.m2i2.view.BasicView;
import edu.fbansept.m2i2.view.ProductBasicView;
import edu.fbansept.m2i2.view.ProductSummaryView;

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
  @JsonView({BasicView.class, ProductBasicView.class})
  protected Integer id;

  @NotBlank(groups = { add.class, update.class })
  @Size(min = 2, max = 100, groups = { add.class, update.class })
  @Column(unique = true, nullable = false)
  @JsonView({BasicView.class, ProductBasicView.class})
  protected String name;

  @DecimalMin(value = "0.01", groups = { add.class, update.class })
  @DecimalMax(value = "99999.99", groups = { add.class, update.class })
  @NotNull(groups = { add.class, update.class })
  @Column(columnDefinition = "DECIMAL(8,2)")
  @JsonView({BasicView.class, ProductBasicView.class})
  protected Double price;

  @Size(max = 500, groups = { add.class, update.class })
  @Column(columnDefinition = "TEXT")
  @JsonView({BasicView.class, ProductBasicView.class})
  protected String description;

  @Min(value = 0, groups = { add.class, update.class })
  @Column(nullable = false)
  @JsonView({BasicView.class, ProductBasicView.class})
  protected Integer stock = 0;

  @ManyToOne
  @JoinColumn(name = "admin_id", nullable = false)
  @NotNull(groups = { add.class, update.class })
  @JsonView(ProductSummaryView.class)
  protected User admin;

  @ManyToOne
  @JoinColumn(name = "seller_id", nullable = false)
  @NotNull(groups = { add.class, update.class })
  @JsonView(ProductSummaryView.class)
  protected User seller;

  @ManyToOne
  @JoinColumn(name = "client_id")
  @JsonView(ProductSummaryView.class)
  protected User client;
}
