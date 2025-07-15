package edu.fbansept.m2i2.view;

/**
 * List view for Product entities.
 * Extends ProductBasicView and is specifically optimized for displaying products in lists.
 * This view provides the minimal necessary information for product list displays
 * while maintaining optimal performance for large datasets.
 * 
 * Fields included:
 * - All ProductBasicView fields (id, name, price, description, stock)
 * 
 * This view is designed for:
 * - Product selection dropdowns
 * - Product list tables
 * - Quick product references
 * - Paginated product lists
 * - Product catalog listings
 * - Search result displays
 * 
 * Performance optimized for:
 * - Minimal payload size
 * - Fast serialization
 * - Reduced network overhead
 * - Efficient list rendering
 * 
 * Use cases:
 * - E-commerce product grids
 * - Admin product management lists
 * - Product selection interfaces
 * - Inventory listing displays
 * - Product search results
 * - Public product catalogs
 */
public class ProductListView extends ProductBasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(ProductListView.class) will include:
    // - Product ID (inherited from ProductBasicView)
    // - Product name (inherited from ProductBasicView)
    // - Product price (inherited from ProductBasicView)
    // - Product description (inherited from ProductBasicView)
    // - Product stock (inherited from ProductBasicView)
    // 
    // Optimized for list displays with minimal data transfer
    // Excludes: admin, seller, client relationships (for maximum performance)
    // Perfect for scenarios where only core product information is needed
}