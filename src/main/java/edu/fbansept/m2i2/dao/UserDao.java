package edu.fbansept.m2i2.dao;

import edu.fbansept.m2i2.model.Role;
import edu.fbansept.m2i2.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
  List<User> findByRole(Role role);
}
