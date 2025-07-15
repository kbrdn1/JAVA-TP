package edu.fbansept.m2i2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.m2i2.annotation.MeasureTime;
import edu.fbansept.m2i2.view.JsonViews;
import edu.fbansept.m2i2.dao.ProductDao;
import edu.fbansept.m2i2.dao.RoleDao;
import edu.fbansept.m2i2.dao.UserDao;
import edu.fbansept.m2i2.dto.ProductDetailDTO;
import edu.fbansept.m2i2.model.Product;
import edu.fbansept.m2i2.model.Role;
import edu.fbansept.m2i2.model.User;
import edu.fbansept.m2i2.service.ProductMappingService;
import edu.fbansept.m2i2.service.ProductValidationService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  protected UserDao userDao;

  @Autowired
  protected RoleDao roleDao;

  @Autowired
  protected ProductDao productDao;

  @Autowired
  protected ProductValidationService validationService;

  @Autowired
  protected ProductMappingService mappingService;

  @GetMapping
  @MeasureTime(message = "Retrieving all users")
  @JsonView(JsonViews.User.List.class)
  public List<User> getAll() {
    return userDao.findAll();
  }

  @GetMapping("/{id}")
  @MeasureTime(message = "Retrieving user by ID", includeParameters = true)
  @JsonView(JsonViews.User.Detail.class)
  public ResponseEntity<User> get(@PathVariable int id) {
    Optional<User> userOptional = userDao.findById(id);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/role/{roleId}")
  @MeasureTime(message = "Retrieving users by role ID", includeParameters = true)
  @JsonView(JsonViews.User.Summary.class)
  public ResponseEntity<List<User>> getByRoleId(@PathVariable int roleId) {
    Optional<Role> roleOptional = roleDao.findById(roleId);

    if (roleOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<User> roleUsers = userDao.findByRole(roleOptional.get());
    return new ResponseEntity<>(roleUsers, HttpStatus.OK);
  }

  @GetMapping("/{userId}/admin-products")
  @MeasureTime(message = "Retrieving admin products by user ID", includeParameters = true)
  public ResponseEntity<List<ProductDetailDTO>> getAdminProductsByUserId(@PathVariable int userId) {
    Optional<User> userOptional = userDao.findById(userId);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<Product> adminProducts = productDao.findByAdmin(userOptional.get());
    List<ProductDetailDTO> adminProductDTOs = mappingService.toProductDetailDTOList(adminProducts);
    return new ResponseEntity<>(adminProductDTOs, HttpStatus.OK);
  }

  @GetMapping("/{userId}/seller-products")
  @MeasureTime(message = "Retrieving seller products by user ID", includeParameters = true)
  public ResponseEntity<List<ProductDetailDTO>> getSellerProductsByUserId(
    @PathVariable int userId
  ) {
    Optional<User> userOptional = userDao.findById(userId);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<Product> sellerProducts = productDao.findBySeller(userOptional.get());
    List<ProductDetailDTO> sellerProductDTOs = mappingService.toProductDetailDTOList(
      sellerProducts
    );
    return new ResponseEntity<>(sellerProductDTOs, HttpStatus.OK);
  }

  @GetMapping("/{userId}/client-products")
  @MeasureTime(message = "Retrieving client products by user ID", includeParameters = true)
  public ResponseEntity<List<ProductDetailDTO>> getClientProductsByUserId(
    @PathVariable int userId
  ) {
    Optional<User> userOptional = userDao.findById(userId);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<Product> clientProducts = productDao.findByClient(userOptional.get());
    List<ProductDetailDTO> clientProductDTOs = mappingService.toProductDetailDTOList(
      clientProducts
    );
    return new ResponseEntity<>(clientProductDTOs, HttpStatus.OK);
  }

  @PostMapping
  @MeasureTime(message = "Creating a new user", logLevel = "DEBUG")
  @JsonView(JsonViews.User.Summary.class)
  public ResponseEntity<?> add(
    @RequestBody @Validated(User.add.class) User userSent,
    @RequestParam(required = false) Integer roleId
  ) {
    // If roleId is provided, set the role relationship
    if (roleId != null) {
      Optional<Role> roleOptional = roleDao.findById(roleId);
      if (roleOptional.isEmpty()) {
        return new ResponseEntity<>("Role not found", HttpStatus.BAD_REQUEST);
      }
      userSent.setRole(roleOptional.get());
    }
    userDao.save(userSent);

    return new ResponseEntity<>(userSent, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @MeasureTime(message = "Deleting user", includeParameters = true, logLevel = "WARN")
  public ResponseEntity<?> delete(@PathVariable int id) {
    Optional<User> userOptional = userDao.findById(id);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    userDao.deleteById(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/{id}")
  @MeasureTime(message = "Updating user", includeParameters = true)
  @JsonView(JsonViews.User.Summary.class)
  public ResponseEntity<?> update(
    @PathVariable int id,
    @RequestBody @Validated(User.update.class) User userSent,
    @RequestParam(required = false) Integer roleId
  ) {
    userSent.setId(id);

    Optional<User> userOptional = userDao.findById(id);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // If roleId is provided, update the role relationship
    if (roleId != null) {
      Optional<Role> roleOptional = roleDao.findById(roleId);
      if (roleOptional.isEmpty()) {
        return new ResponseEntity<>("Role not found", HttpStatus.BAD_REQUEST);
      }
      userSent.setRole(roleOptional.get());
    } else {
      // Keep the existing role relationship
      userSent.setRole(userOptional.get().getRole());
    }

    // Force not to update the password
    // We assign the old password to the user to be saved
    userSent.setPassword(userOptional.get().getPassword());

    userDao.save(userSent);

    return new ResponseEntity<>(userSent, HttpStatus.OK);
  }

  // New endpoints using @JsonView directly with User entities

  @GetMapping("/basic")
  @MeasureTime(message = "Retrieving basic user information")
  @JsonView(JsonViews.User.Basic.class)
  public List<User> getBasicUsers() {
    return userDao.findAll();
  }

  @GetMapping("/summary")
  @MeasureTime(message = "Retrieving user summary with roles")
  @JsonView(JsonViews.User.Summary.class)
  public List<User> getUsersSummary() {
    return userDao.findAll();
  }

  @GetMapping("/entity/{id}")
  @MeasureTime(message = "Retrieving user entity by ID", includeParameters = true)
  @JsonView(JsonViews.User.Summary.class)
  public ResponseEntity<User> getUserEntity(@PathVariable int id) {
    Optional<User> userOptional = userDao.findById(id);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/entity/{id}/basic")
  @MeasureTime(message = "Retrieving basic user entity by ID", includeParameters = true)
  @JsonView(JsonViews.User.Basic.class)
  public ResponseEntity<User> getUserEntityBasic(@PathVariable int id) {
    Optional<User> userOptional = userDao.findById(id);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/with-role")
  @MeasureTime(message = "Retrieving users with role information")
  @JsonView(JsonViews.User.Summary.class)
  public List<User> getUsersWithRole() {
    return userDao.findAll();
  }

  @GetMapping("/list-view")
  @MeasureTime(message = "Retrieving users in list view")
  @JsonView(JsonViews.User.List.class)
  public List<User> getUsersListView() {
    return userDao.findAll();
  }
}
