package edu.fbansept.m2i2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

  public interface add {}

  public interface update {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Integer id;

  @Column(nullable = false, unique = true)
  @NotBlank(groups = { add.class, update.class })
  @Email(groups = { add.class, update.class })
  @Pattern(
    regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
    groups = { add.class, update.class },
    message = "Email is malformed"
  )
  protected String email;

  @Column(nullable = false)
  @NotBlank(groups = { add.class })
  protected String password;

  @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference("user-admin-products")
  protected List<Product> adminProducts = new ArrayList<>();

  @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference("user-seller-products")
  protected List<Product> sellerProducts = new ArrayList<>();

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  @JsonManagedReference("user-client-products")
  protected List<Product> clientProducts = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "role_id")
  @JsonBackReference("role-users")
  protected Role role;
}
