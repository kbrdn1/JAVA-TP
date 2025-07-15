package edu.fbansept.m2i2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.m2i2.annotation.MeasureTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import edu.fbansept.m2i2.view.UserBasicView;
import edu.fbansept.m2i2.view.UserSummaryView;
import edu.fbansept.m2i2.view.UserDetailView;
import edu.fbansept.m2i2.view.UserListView;
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
@Tag(name = "👥 User Management", description = "Complete user lifecycle management with role-based access control and JsonView optimization")
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
  @JsonView(UserListView.class)
  @Operation(
    summary = "Get all users (List View)",
    description = "Retrieves all users using UserListView for optimal performance. " +
                  "Returns minimal user data (id, email) perfect for lists and dropdowns. " +
                  "Payload: ~50 bytes per user.",
    tags = {"👥 User Management", "⚡ Performance Optimized"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Users retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "UserListView Example",
          value = "[{\"id\": 1, \"email\": \"john.client@example.com\"}, {\"id\": 2, \"email\": \"jane.admin@example.com\"}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<User> getAll() {
    return userDao.findAll();
  }

  @GetMapping("/{id}")
  @MeasureTime(message = "Retrieving user by ID", includeParameters = true)
  @JsonView(UserDetailView.class)
  @Operation(
    summary = "Get user by ID (Detail View)",
    description = "Retrieves a specific user with complete information including role and all product relationships. " +
                  "Uses UserDetailView providing comprehensive data. Payload: ~500-2000 bytes.",
    tags = {"👥 User Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User found and returned with complete details",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "UserDetailView Example",
          value = "{\"id\": 1, \"email\": \"john.client@example.com\", \"role\": {\"id\": 1, \"name\": \"CLIENT\"}, \"adminProducts\": [], \"sellerProducts\": [], \"clientProducts\": [{\"id\": 1, \"name\": \"Laptop\", \"price\": 999.99}]}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "id", description = "User ID", required = true, example = "1")
  public ResponseEntity<User> get(@PathVariable int id) {
    Optional<User> userOptional = userDao.findById(id);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/role/{roleId}")
  @MeasureTime(message = "Retrieving users by role ID", includeParameters = true)
  @JsonView(UserSummaryView.class)
  @Operation(
    summary = "Get users by role ID",
    description = "Retrieves all users assigned to a specific role. " +
                  "Uses UserSummaryView including basic user info and role details. " +
                  "Payload: ~120 bytes per user.",
    tags = {"👥 User Management", "🎭 Role Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Users with specified role retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "Users by Role Example",
          value = "[{\"id\": 1, \"email\": \"john.client@example.com\", \"role\": {\"id\": 1, \"name\": \"CLIENT\"}}]"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Role not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "roleId", description = "Role ID to filter users", required = true, example = "1")
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
  @JsonView(UserSummaryView.class)
  @Operation(
    summary = "Create a new user",
    description = "Creates a new user with optional role assignment. " +
                  "Returns the created user in UserSummaryView format. " +
                  "Email must be unique and valid.",
    tags = {"👥 User Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "User created successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "Created User Example",
          value = "{\"id\": 6, \"email\": \"newuser@example.com\", \"role\": {\"id\": 1, \"name\": \"CLIENT\"}}"
        )
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid user data or role not found"),
    @ApiResponse(responseCode = "409", description = "Email already exists")
  })
  public ResponseEntity<?> add(
    @Parameter(description = "User data to create", required = true)
    @RequestBody @Validated(User.add.class) User userSent,
    @Parameter(description = "Optional role ID to assign to the user", example = "1")
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
  @JsonView(UserSummaryView.class)
  @Operation(
    summary = "Update user by ID",
    description = "Updates an existing user's information. Password cannot be updated through this endpoint for security. " +
                  "Role can be changed by providing roleId parameter.",
    tags = {"👥 User Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User updated successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "Updated User Example",
          value = "{\"id\": 1, \"email\": \"updated@example.com\", \"role\": {\"id\": 2, \"name\": \"ADMIN\"}}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "400", description = "Invalid user data or role not found"),
    @ApiResponse(responseCode = "409", description = "Email already exists")
  })
  public ResponseEntity<?> update(
    @Parameter(description = "User ID to update", required = true, example = "1")
    @PathVariable int id,
    @Parameter(description = "Updated user data", required = true)
    @RequestBody @Validated(User.update.class) User userSent,
    @Parameter(description = "Optional new role ID for the user", example = "2")
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
  @JsonView(UserBasicView.class)
  @Operation(
    summary = "Get users (Basic View) - Ultra Light",
    description = "Retrieves all users with minimal data using UserBasicView. " +
                  "Perfect for dropdowns, selections, and high-performance lists. " +
                  "Payload: ~50 bytes per user. Includes only ID and email.",
    tags = {"🔄 User JsonViews", "⚡ Performance Optimized"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Basic user information retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "UserBasicView Example",
          value = "[{\"id\": 1, \"email\": \"john.client@example.com\"}, {\"id\": 2, \"email\": \"jane.admin@example.com\"}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<User> getBasicUsers() {
    return userDao.findAll();
  }

  @GetMapping("/summary")
  @MeasureTime(message = "Retrieving user summary with roles")
  @JsonView(UserSummaryView.class)
  @Operation(
    summary = "Get users (Summary View) - With Roles",
    description = "Retrieves all users with role information using UserSummaryView. " +
                  "Perfect for user cards, management interfaces, and role-aware displays. " +
                  "Payload: ~120 bytes per user. Includes ID, email, and role details.",
    tags = {"🔄 User JsonViews", "🎭 Role Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User summary with roles retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "UserSummaryView Example",
          value = "[{\"id\": 1, \"email\": \"john.client@example.com\", \"role\": {\"id\": 1, \"name\": \"CLIENT\"}}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<User> getUsersSummary() {
    return userDao.findAll();
  }

  @GetMapping("/entity/{id}")
  @MeasureTime(message = "Retrieving user entity by ID", includeParameters = true)
  @JsonView(UserSummaryView.class)
  @Operation(
    summary = "Get user entity (Summary View)",
    description = "Retrieves a specific user entity with summary information including role. " +
                  "Alternative to the detail endpoint when product relationships aren't needed. " +
                  "Payload: ~120 bytes.",
    tags = {"🔄 User JsonViews"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User entity retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "User Entity Summary Example",
          value = "{\"id\": 1, \"email\": \"john.client@example.com\", \"role\": {\"id\": 1, \"name\": \"CLIENT\"}}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "id", description = "User ID", required = true, example = "1")
  public ResponseEntity<User> getUserEntity(@PathVariable int id) {
    Optional<User> userOptional = userDao.findById(id);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/entity/{id}/basic")
  @MeasureTime(message = "Retrieving basic user entity by ID", includeParameters = true)
  @JsonView(UserBasicView.class)
  @Operation(
    summary = "Get user entity (Basic View) - Minimal",
    description = "Retrieves a specific user entity with minimal information using UserBasicView. " +
                  "Ultra-lightweight endpoint for when only ID and email are needed. " +
                  "Payload: ~50 bytes.",
    tags = {"🔄 User JsonViews", "⚡ Performance Optimized"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Basic user entity retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "User Entity Basic Example",
          value = "{\"id\": 1, \"email\": \"john.client@example.com\"}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "id", description = "User ID", required = true, example = "1")
  public ResponseEntity<User> getUserEntityBasic(@PathVariable int id) {
    Optional<User> userOptional = userDao.findById(id);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/with-role")
  @MeasureTime(message = "Retrieving users with role information")
  @JsonView(UserSummaryView.class)
  @Operation(
    summary = "Get users with role information",
    description = "Retrieves all users including their role information. " +
                  "Semantically equivalent to /summary but provides clear intent for role-aware operations. " +
                  "Payload: ~120 bytes per user.",
    tags = {"🔄 User JsonViews", "🎭 Role Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Users with role information retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "Users with Roles Example",
          value = "[{\"id\": 1, \"email\": \"john.client@example.com\", \"role\": {\"id\": 1, \"name\": \"CLIENT\"}}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<User> getUsersWithRole() {
    return userDao.findAll();
  }

  @GetMapping("/list-view")
  @MeasureTime(message = "Retrieving users in list view")
  @JsonView(UserListView.class)
  @Operation(
    summary = "Get users (List View) - Optimized for Large Lists",
    description = "Retrieves all users using UserListView, specifically optimized for large datasets and list displays. " +
                  "Semantically equivalent to /basic but clearly indicates list optimization intent. " +
                  "Perfect for paginated lists, tables, and selection interfaces. " +
                  "Payload: ~50 bytes per user.",
    tags = {"🔄 User JsonViews", "⚡ Performance Optimized"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Optimized user list retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "UserListView Example",
          value = "[{\"id\": 1, \"email\": \"john.client@example.com\"}, {\"id\": 2, \"email\": \"jane.admin@example.com\"}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<User> getUsersListView() {
    return userDao.findAll();
  }
}
