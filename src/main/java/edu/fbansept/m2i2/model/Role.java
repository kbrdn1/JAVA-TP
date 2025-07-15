package edu.fbansept.m2i2.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import edu.fbansept.m2i2.view.JsonViews;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

  public interface add {}

  public interface update {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(JsonViews.Role.Basic.class)
  protected Integer id;

  @NotBlank(groups = { add.class, update.class })
  @Column(unique = true, nullable = false)
  @JsonView(JsonViews.Role.Basic.class)
  protected String name;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonView(JsonViews.Role.WithUsers.class)
  protected List<User> users;
}
