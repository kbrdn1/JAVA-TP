package edu.fbansept.m2i2.dao;

import edu.fbansept.m2i2.model.Product;
import edu.fbansept.m2i2.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
  List<Product> findByAdmin(User admin);
  List<Product> findBySeller(User seller);
  List<Product> findByClient(User client);
}
