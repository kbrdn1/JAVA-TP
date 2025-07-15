package edu.fbansept.m2i2.controller;

import edu.fbansept.m2i2.annotation.MeasureTime;
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
  public List<ProductDetailDTO> getAll() {
    List<Product> products = productDao.findAll();
    return mappingService.toProductDetailDTOList(products);
  }

  @GetMapping("/{id}")
  @MeasureTime(message = "Retrieving product by ID", includeParameters = true)
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
  public ResponseEntity<?> add(
    @RequestBody @Validated(Product.add.class) Product productSent,
    @RequestParam Integer adminId,
    @RequestParam Integer sellerId,
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
  public ResponseEntity<?> update(
    @PathVariable int id,
    @RequestBody @Validated(Product.update.class) Product productSent,
    @RequestParam(required = false) Integer adminId,
    @RequestParam(required = false) Integer sellerId,
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
  public List<ProductDetailDTO> getAvailableProducts() {
    List<Product> products = productDao.findAll();
    return products.stream()
            .filter(product -> product.getClient() == null)
            .map(mappingService::toAvailableProductDTO)
            .toList();
  }

  @PostMapping("/{id}/assign-client")
  @MeasureTime(message = "Assigning client to product", includeParameters = true)
  public ResponseEntity<?> assignClient(
    @PathVariable int id,
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
  public ResponseEntity<?> removeClient(@PathVariable int id) {
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
}