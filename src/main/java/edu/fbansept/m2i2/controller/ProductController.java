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
import edu.fbansept.m2i2.view.ProductBasicView;
import edu.fbansept.m2i2.view.ProductSummaryView;
import edu.fbansept.m2i2.view.ProductDetailView;
import edu.fbansept.m2i2.view.ProductListView;
import edu.fbansept.m2i2.view.ProductCatalogView;
import edu.fbansept.m2i2.dao.ProductDao;
import edu.fbansept.m2i2.dao.UserDao;
import edu.fbansept.m2i2.dto.ProductDetailDTO;
import edu.fbansept.m2i2.model.Product;
import edu.fbansept.m2i2.model.User;
import edu.fbansept.m2i2.service.ProductValidationService;
import edu.fbansept.m2i2.service.ProductMappingService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "📦 Product Management", description = "Product lifecycle with strict business constraints and JsonView optimization")
public class ProductController {

  @Autowired
  protected ProductDao productDao;
  
  @Autowired
  protected UserDao userDao;
  
  @Autowired
  protected ProductValidationService validationService;
  
  @Autowired
  protected ProductMappingService mappingService;

  @GetMapping
  @MeasureTime(message = "Retrieving all products")
  @JsonView(ProductListView.class)
  @Operation(
    summary = "Get all products (DTOs with List View)",
    description = "Retrieves all products using DTOs with ProductListView for optimal performance. " +
                  "Returns comprehensive product information through ProductDetailDTO mapping. " +
                  "Ideal for product management interfaces.",
    tags = {"📦 Product Management", "⚡ Performance Optimized"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Products retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductDetailDTO.class),
        examples = @ExampleObject(
          name = "Product DTOs Example",
          value = "[{\"id\": 1, \"name\": \"Laptop Dell XPS 13\", \"price\": 1299.99, \"description\": \"High-performance ultrabook\", \"stock\": 15}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<ProductDetailDTO> getAll() {
    List<Product> products = productDao.findAll();
    return mappingService.toProductDetailDTOList(products);
  }

  @GetMapping("/{id}")
  @MeasureTime(message = "Retrieving product by ID", includeParameters = true)
  @JsonView(ProductDetailView.class)
  @Operation(
    summary = "Get product by ID (DTO with Detail View)",
    description = "Retrieves a specific product with complete information using ProductDetailDTO. " +
                  "Includes all business relationships (admin, seller, client) and comprehensive product data. " +
                  "Perfect for product detail pages and editing interfaces.",
    tags = {"📦 Product Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Product found and returned with complete details",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductDetailDTO.class),
        examples = @ExampleObject(
          name = "Product Detail DTO Example",
          value = "{\"id\": 1, \"name\": \"Laptop Dell XPS 13\", \"price\": 1299.99, \"description\": \"High-performance ultrabook\", \"stock\": 15, \"adminEmail\": \"jane.admin@example.com\", \"sellerEmail\": \"mike.seller@example.com\", \"clientEmail\": \"john.client@example.com\"}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Product not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "id", description = "Product ID", required = true, example = "1")
  public ResponseEntity<ProductDetailDTO> get(@PathVariable int id) {
    Optional<Product> productOptional = productDao.findById(id);

    if (productOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    ProductDetailDTO productDTO = mappingService.toProductDetailDTO(productOptional.get());
    return new ResponseEntity<>(productDTO, HttpStatus.OK);
  }

  @GetMapping("/admin/{userId}")
  @MeasureTime(message = "Retrieving products by admin ID", includeParameters = true)
  public ResponseEntity<List<ProductDetailDTO>> getByAdminId(@PathVariable int userId) {
    Optional<User> userOptional = userDao.findById(userId);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<Product> adminProducts = productDao.findByAdmin(userOptional.get());
    List<ProductDetailDTO> adminProductDTOs = mappingService.toProductDetailDTOList(adminProducts);
    return new ResponseEntity<>(adminProductDTOs, HttpStatus.OK);
  }

  @GetMapping("/seller/{userId}")
  @MeasureTime(message = "Retrieving products by seller ID", includeParameters = true)
  public ResponseEntity<List<ProductDetailDTO>> getBySellerId(@PathVariable int userId) {
    Optional<User> userOptional = userDao.findById(userId);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<Product> sellerProducts = productDao.findBySeller(userOptional.get());
    List<ProductDetailDTO> sellerProductDTOs = mappingService.toProductDetailDTOList(sellerProducts);
    return new ResponseEntity<>(sellerProductDTOs, HttpStatus.OK);
  }

  @GetMapping("/client/{userId}")
  @MeasureTime(message = "Retrieving products by client ID", includeParameters = true)
  public ResponseEntity<List<ProductDetailDTO>> getByClientId(@PathVariable int userId) {
    Optional<User> userOptional = userDao.findById(userId);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<Product> clientProducts = productDao.findByClient(userOptional.get());
    List<ProductDetailDTO> clientProductDTOs = mappingService.toProductDetailDTOList(clientProducts);
    return new ResponseEntity<>(clientProductDTOs, HttpStatus.OK);
  }

  @PostMapping
  @MeasureTime(message = "Creating a new product", logLevel = "DEBUG")
  @JsonView(ProductDetailView.class)
  @Operation(
    summary = "Create a new product with business constraints",
    description = "Creates a new product with strict business validation. " +
                  "🔒 BUSINESS RULES: Products MUST have an Admin (ADMIN role) and Seller (SELLER role). " +
                  "Client (CLIENT role) is optional. All role assignments are validated. " +
                  "Returns ProductDetailDTO with complete business relationship information.",
    tags = {"📦 Product Management", "🏢 Business Operations"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Product created successfully with business constraints validated",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductDetailDTO.class),
        examples = @ExampleObject(
          name = "Created Product Example",
          value = "{\"id\": 9, \"name\": \"New Business Laptop\", \"price\": 1499.99, \"description\": \"Latest business laptop\", \"stock\": 10, \"adminEmail\": \"jane.admin@example.com\", \"sellerEmail\": \"mike.seller@example.com\", \"clientEmail\": null}"
        )
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid product data, missing required roles, or role validation failed"),
    @ApiResponse(responseCode = "404", description = "Admin or Seller user not found"),
    @ApiResponse(responseCode = "409", description = "Product name already exists or business constraint violation")
  })
  public ResponseEntity<?> add(
    @Parameter(description = "Product data to create", required = true)
    @RequestBody @Validated(Product.add.class) Product productSent,
    @Parameter(description = "REQUIRED: User ID with ADMIN role to manage this product", required = true, example = "2")
    @RequestParam Integer adminId,
    @Parameter(description = "REQUIRED: User ID with SELLER role to sell this product", required = true, example = "3")
    @RequestParam Integer sellerId,
    @Parameter(description = "OPTIONAL: User ID with CLIENT role who purchased this product", example = "1")
    @RequestParam(required = false) Integer clientId
  ) {
    // Set and validate admin
    ProductValidationService.ValidationResult adminResult = validationService.setProductAdmin(productSent, adminId);
    if (!adminResult.isValid()) {
      return new ResponseEntity<>(adminResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
    
    // Set and validate seller
    ProductValidationService.ValidationResult sellerResult = validationService.setProductSeller(productSent, sellerId);
    if (!sellerResult.isValid()) {
      return new ResponseEntity<>(sellerResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
    
    // Set and validate client (optional)
    ProductValidationService.ValidationResult clientResult = validationService.setProductClient(productSent, clientId);
    if (!clientResult.isValid()) {
      return new ResponseEntity<>(clientResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
    
    // Final validation of the complete product
    ProductValidationService.ValidationResult productResult = validationService.validateProduct(productSent);
    if (!productResult.isValid()) {
      return new ResponseEntity<>(productResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
    
    productDao.save(productSent);

    ProductDetailDTO productDTO = mappingService.toProductDetailDTO(productSent);
    return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @MeasureTime(message = "Deleting product", includeParameters = true, logLevel = "WARN")
  public ResponseEntity<?> delete(@PathVariable int id) {
    Optional<Product> productOptional = productDao.findById(id);

    if (productOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    productDao.deleteById(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/{id}")
  @MeasureTime(message = "Updating product", includeParameters = true)
  @JsonView(ProductDetailView.class)
  @Operation(
    summary = "Update product with business constraint validation",
    description = "Updates an existing product while maintaining business constraints. " +
                  "🔒 BUSINESS RULES: Products must always have Admin + Seller roles. " +
                  "Role changes are validated. Client can be assigned/removed. " +
                  "Returns updated ProductDetailDTO with complete business relationships.",
    tags = {"📦 Product Management", "🏢 Business Operations"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Product updated successfully with business constraints validated",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductDetailDTO.class),
        examples = @ExampleObject(
          name = "Updated Product Example",
          value = "{\"id\": 1, \"name\": \"Updated Laptop Model\", \"price\": 1399.99, \"description\": \"Updated description\", \"stock\": 12, \"adminEmail\": \"bob.admin@example.com\", \"sellerEmail\": \"mike.seller@example.com\", \"clientEmail\": \"sarah.client@example.com\"}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Product not found or specified user not found"),
    @ApiResponse(responseCode = "400", description = "Invalid product data or role validation failed"),
    @ApiResponse(responseCode = "409", description = "Product name conflict or business constraint violation")
  })
  public ResponseEntity<?> update(
    @Parameter(description = "Product ID to update", required = true, example = "1")
    @PathVariable int id,
    @Parameter(description = "Updated product data", required = true)
    @RequestBody @Validated(Product.update.class) Product productSent,
    @Parameter(description = "Optional new Admin ID (must have ADMIN role)", example = "5")
    @RequestParam(required = false) Integer adminId,
    @Parameter(description = "Optional new Seller ID (must have SELLER role)", example = "3")
    @RequestParam(required = false) Integer sellerId,
    @Parameter(description = "Optional new Client ID (must have CLIENT role, or null to remove)", example = "4")
    @RequestParam(required = false) Integer clientId
  ) {
    productSent.setId(id);

    Optional<Product> productOptional = productDao.findById(id);

    if (productOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Product existingProduct = productOptional.get();

    // Update admin if provided, otherwise keep existing
    if (adminId != null) {
      ProductValidationService.ValidationResult adminResult = validationService.setProductAdmin(productSent, adminId);
      if (!adminResult.isValid()) {
        return new ResponseEntity<>(adminResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
      }
    } else {
      productSent.setAdmin(existingProduct.getAdmin());
    }

    // Update seller if provided, otherwise keep existing
    if (sellerId != null) {
      ProductValidationService.ValidationResult sellerResult = validationService.setProductSeller(productSent, sellerId);
      if (!sellerResult.isValid()) {
        return new ResponseEntity<>(sellerResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
      }
    } else {
      productSent.setSeller(existingProduct.getSeller());
    }

    // Update client if provided (can be null to remove client)
    if (clientId != null) {
      ProductValidationService.ValidationResult clientResult = validationService.setProductClient(productSent, clientId);
      if (!clientResult.isValid()) {
        return new ResponseEntity<>(clientResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
      }
    } else {
      productSent.setClient(existingProduct.getClient());
    }

    // Final validation of the updated product
    ProductValidationService.ValidationResult productResult = validationService.validateProduct(productSent);
    if (!productResult.isValid()) {
      return new ResponseEntity<>(productResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

    productDao.save(productSent);

    ProductDetailDTO productDTO = mappingService.toProductDetailDTO(productSent);
    return new ResponseEntity<>(productDTO, HttpStatus.OK);
  }

  @GetMapping("/available")
  @MeasureTime(message = "Retrieving available products (without clients)")
  @JsonView(ProductCatalogView.class)
  @Operation(
    summary = "Get available products (No clients assigned)",
    description = "Retrieves products that are available for purchase (no client assigned). " +
                  "🛒 BUSINESS LOGIC: Filters products where client is null, indicating availability. " +
                  "Perfect for e-commerce available inventory and purchase workflows. " +
                  "Returns ProductDetailDTOs with availability-specific mapping.",
    tags = {"🏢 Business Operations", "🛒 E-commerce"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Available products retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductDetailDTO.class),
        examples = @ExampleObject(
          name = "Available Products Example",
          value = "[{\"id\": 4, \"name\": \"Monitor 4K 27-inch\", \"price\": 399.99, \"description\": \"4K display\", \"stock\": 8, \"adminEmail\": \"jane.admin@example.com\", \"sellerEmail\": \"mike.seller@example.com\", \"clientEmail\": null, \"available\": true}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<ProductDetailDTO> getAvailableProducts() {
    List<Product> products = productDao.findAll();
    return products.stream()
            .filter(product -> product.getClient() == null)
            .map(mappingService::toAvailableProductDTO)
            .toList();
  }

  @PostMapping("/{id}/assign-client")
  @MeasureTime(message = "Assigning client to product", includeParameters = true)
  @Operation(
    summary = "Assign client to product (Purchase workflow)",
    description = "Assigns a client to a product, marking it as purchased. " +
                  "🛒 BUSINESS WORKFLOW: Validates client role, checks product availability, updates business state. " +
                  "Product must be available (no existing client). Client must have CLIENT role.",
    tags = {"🏢 Business Operations", "🛒 E-commerce"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Client assigned to product successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductDetailDTO.class),
        examples = @ExampleObject(
          name = "Product with Assigned Client",
          value = "{\"id\": 4, \"name\": \"Monitor 4K\", \"clientEmail\": \"sarah.client@example.com\", \"available\": false}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Product or client not found"),
    @ApiResponse(responseCode = "400", description = "Invalid client role or business constraint violation"),
    @ApiResponse(responseCode = "409", description = "Product already has a client assigned")
  })
  public ResponseEntity<?> assignClient(
    @Parameter(description = "Product ID to assign client to", required = true, example = "4")
    @PathVariable int id,
    @Parameter(description = "Client user ID (must have CLIENT role)", required = true, example = "1")
    @RequestParam Integer clientId
  ) {
    Optional<Product> productOptional = productDao.findById(id);

    if (productOptional.isEmpty()) {
      return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    Product product = productOptional.get();

    // Check if product already has a client
    if (product.getClient() != null) {
      return new ResponseEntity<>("Product already has a client", HttpStatus.CONFLICT);
    }

    // Validate and set client
    ProductValidationService.ValidationResult clientResult = validationService.setProductClient(product, clientId);
    if (!clientResult.isValid()) {
      return new ResponseEntity<>(clientResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

    productDao.save(product);

    ProductDetailDTO productDTO = mappingService.toProductDetailDTO(product);
    return new ResponseEntity<>(productDTO, HttpStatus.OK);
  }

  @PostMapping("/{id}/remove-client")
  @MeasureTime(message = "Removing client from product", includeParameters = true)
  @Operation(
    summary = "Remove client from product (Return/Refund workflow)",
    description = "Removes client assignment from a product, marking it as available again. " +
                  "🔄 BUSINESS WORKFLOW: Product return/refund process, inventory management. " +
                  "Makes product available for other clients to purchase.",
    tags = {"🏢 Business Operations", "🔄 Returns Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Client removed from product successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductDetailDTO.class),
        examples = @ExampleObject(
          name = "Product After Client Removal",
          value = "{\"id\": 1, \"name\": \"Laptop Dell XPS 13\", \"clientEmail\": null, \"available\": true}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Product not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public ResponseEntity<?> removeClient(
    @Parameter(description = "Product ID to remove client from", required = true, example = "1")
    @PathVariable int id) {
    Optional<Product> productOptional = productDao.findById(id);

    if (productOptional.isEmpty()) {
      return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    Product product = productOptional.get();
    product.setClient(null);
    productDao.save(product);

    ProductDetailDTO productDTO = mappingService.toProductDetailDTO(product);
    return new ResponseEntity<>(productDTO, HttpStatus.OK);
  }

  @GetMapping("/role-view/{userId}")
  @MeasureTime(message = "Retrieving products with role-based view", includeParameters = true)
  public ResponseEntity<List<ProductDetailDTO>> getRoleBasedView(@PathVariable int userId) {
    Optional<User> userOptional = userDao.findById(userId);

    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    User user = userOptional.get();
    String userRole = user.getRole() != null ? user.getRole().getName() : "UNKNOWN";

    List<Product> products = productDao.findAll();
    List<ProductDetailDTO> roleBasedProducts = products.stream()
            .map(product -> mappingService.toRoleBasedProductDTO(product, userRole))
            .toList();

    return new ResponseEntity<>(roleBasedProducts, HttpStatus.OK);
  }

  @GetMapping("/business-summary")
  @MeasureTime(message = "Retrieving business summary of products")
  public Map<String, Object> getBusinessSummary() {
    List<Product> allProducts = productDao.findAll();
    
    Map<String, Object> summary = new HashMap<>();
    summary.put("totalProducts", allProducts.size());
    summary.put("availableProducts", allProducts.stream().filter(p -> p.getClient() == null).count());
    summary.put("soldProducts", allProducts.stream().filter(p -> p.getClient() != null).count());
    
    // Count by admin
    Map<String, Long> productsByAdmin = allProducts.stream()
            .filter(p -> p.getAdmin() != null)
            .collect(java.util.stream.Collectors.groupingBy(
                p -> p.getAdmin().getEmail(),
                java.util.stream.Collectors.counting()
            ));
    summary.put("productsByAdmin", productsByAdmin);
    
    // Count by seller
    Map<String, Long> productsBySeller = allProducts.stream()
            .filter(p -> p.getSeller() != null)
            .collect(java.util.stream.Collectors.groupingBy(
                p -> p.getSeller().getEmail(),
                java.util.stream.Collectors.counting()
            ));
    summary.put("productsBySeller", productsBySeller);
    
    return summary;
  }

  // New endpoints using @JsonView directly with Product entities

  @GetMapping("/catalog")
  @MeasureTime(message = "Retrieving product catalog")
  @JsonView(ProductCatalogView.class)
  @Operation(
    summary = "Get product catalog (Public Safe View)",
    description = "Retrieves all products using ProductCatalogView - safe for public consumption. " +
                  "🌐 PUBLIC API: No business relationships (admin/seller/client) exposed. " +
                  "Perfect for e-commerce websites, customer-facing catalogs, and external integrations. " +
                  "Payload: ~150 bytes per product.",
    tags = {"🔄 Product JsonViews", "🛡️ Security Features", "⚡ Performance Optimized"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Public product catalog retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Product.class),
        examples = @ExampleObject(
          name = "ProductCatalogView Example",
          value = "[{\"id\": 1, \"name\": \"Laptop Dell XPS 13\", \"price\": 1299.99, \"description\": \"High-performance ultrabook\", \"stock\": 15}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<Product> getCatalog() {
    return productDao.findAll();
  }

  @GetMapping("/entity/{id}")
  @MeasureTime(message = "Retrieving product entity by ID", includeParameters = true)
  @JsonView(ProductSummaryView.class)
  @Operation(
    summary = "Get product entity (Summary View) - With Business Relationships",
    description = "Retrieves a specific product entity using ProductSummaryView including business relationships. " +
                  "🏢 BUSINESS VIEW: Shows admin, seller, and client assignments with basic user info. " +
                  "Perfect for business management interfaces. Payload: ~350-500 bytes.",
    tags = {"🔄 Product JsonViews", "🏢 Business Operations"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Product entity with business relationships retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Product.class),
        examples = @ExampleObject(
          name = "ProductSummaryView Example",
          value = "{\"id\": 1, \"name\": \"Laptop Dell XPS 13\", \"price\": 1299.99, \"description\": \"High-performance ultrabook\", \"stock\": 15, \"admin\": {\"id\": 2, \"email\": \"jane.admin@example.com\"}, \"seller\": {\"id\": 3, \"email\": \"mike.seller@example.com\"}, \"client\": {\"id\": 1, \"email\": \"john.client@example.com\"}}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Product not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "id", description = "Product ID", required = true, example = "1")
  public ResponseEntity<Product> getProductEntity(@PathVariable int id) {
    Optional<Product> productOptional = productDao.findById(id);

    if (productOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/entity/{id}/detail")
  @MeasureTime(message = "Retrieving detailed product entity by ID", includeParameters = true)
  @JsonView(ProductDetailView.class)
  @Operation(
    summary = "Get product entity (Detail View) - Complete Information",
    description = "Retrieves a specific product entity using ProductDetailView with comprehensive information. " +
                  "📊 DETAIL VIEW: Includes complete business relationships with detailed user information. " +
                  "Perfect for product editing interfaces and comprehensive product management. " +
                  "Payload: ~800-1500 bytes.",
    tags = {"🔄 Product JsonViews"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Complete product entity details retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Product.class),
        examples = @ExampleObject(
          name = "ProductDetailView Example",
          value = "{\"id\": 1, \"name\": \"Laptop Dell XPS 13\", \"price\": 1299.99, \"description\": \"High-performance ultrabook with 13-inch display\", \"stock\": 15, \"admin\": {\"id\": 2, \"email\": \"jane.admin@example.com\", \"role\": {\"id\": 2, \"name\": \"ADMIN\"}}, \"seller\": {\"id\": 3, \"email\": \"mike.seller@example.com\", \"role\": {\"id\": 3, \"name\": \"SELLER\"}}, \"client\": {\"id\": 1, \"email\": \"john.client@example.com\", \"role\": {\"id\": 1, \"name\": \"CLIENT\"}}}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Product not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "id", description = "Product ID", required = true, example = "1")
  public ResponseEntity<Product> getProductEntityDetail(@PathVariable int id) {
    Optional<Product> productOptional = productDao.findById(id);

    if (productOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/basic")
  @MeasureTime(message = "Retrieving basic product information")
  @JsonView(ProductBasicView.class)
  @Operation(
    summary = "Get products (Basic View) - Core Information Only",
    description = "Retrieves all products using ProductBasicView with core information only. " +
                  "📦 BASIC VIEW: Includes id, name, price, description, stock - no relationships. " +
                  "Perfect for internal product lists and inventory management. " +
                  "Payload: ~150 bytes per product.",
    tags = {"🔄 Product JsonViews", "⚡ Performance Optimized"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Basic product information retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Product.class),
        examples = @ExampleObject(
          name = "ProductBasicView Example",
          value = "[{\"id\": 1, \"name\": \"Laptop Dell XPS 13\", \"price\": 1299.99, \"description\": \"High-performance ultrabook\", \"stock\": 15}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<Product> getBasicProducts() {
    return productDao.findAll();
  }

  @GetMapping("/with-users")
  @MeasureTime(message = "Retrieving products with user information")
  @JsonView(ProductSummaryView.class)
  @Operation(
    summary = "Get products (Summary View) - With Business Users",
    description = "Retrieves all products using ProductSummaryView including business user relationships. " +
                  "🏢 BUSINESS VIEW: Shows admin, seller, and client assignments for all products. " +
                  "Perfect for business oversight and relationship management. " +
                  "Payload: ~350-500 bytes per product.",
    tags = {"🔄 Product JsonViews", "🏢 Business Operations"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Products with user relationships retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Product.class),
        examples = @ExampleObject(
          name = "ProductSummaryView List Example",
          value = "[{\"id\": 1, \"name\": \"Laptop Dell XPS 13\", \"price\": 1299.99, \"admin\": {\"id\": 2, \"email\": \"jane.admin@example.com\"}, \"seller\": {\"id\": 3, \"email\": \"mike.seller@example.com\"}, \"client\": {\"id\": 1, \"email\": \"john.client@example.com\"}}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<Product> getProductsWithUsers() {
    return productDao.findAll();
  }
}