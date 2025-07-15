package edu.fbansept.m2i2.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import edu.fbansept.m2i2.view.BasicView;
import edu.fbansept.m2i2.view.RoleBasicView;
import edu.fbansept.m2i2.view.RoleWithUsersView;

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
  @JsonView({BasicView.class, RoleBasicView.class})
  protected Integer id;

  @NotBlank(groups = { add.class, update.class })
  @Column(unique = true, nullable = false)
  @JsonView({BasicView.class, RoleBasicView.class})
  protected String name;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonView(RoleWithUsersView.class)
  protected List<User> users;
}
