package edu.fbansept.m2i2.view;

/**
 * Basic view for Product entities.
 * Includes only essential product information: id, name, price, description, and stock.
 * Excludes user relationships (admin, seller, client) for optimal performance in product lists and catalogs.
 * This view is optimal for product catalogs, product lists, and basic product references.
 * 
 * Fields included:
 * - Product ID
 * - Product name
 * - Product price
 * - Product description
 * - Product stock
 * 
 * Excludes: admin, seller, client relationships (for performance optimization)
 * 
 * Use cases:
 * - Public product catalogs
 * - Product listing pages
 * - Product search results
 * - E-commerce product displays
 * - Product selection dropdowns
 */
public class ProductBasicView extends BasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(ProductBasicView.class) will include:
    // - Product ID
    // - Product name
    // - Product price
    // - Product description
    // - Product stock
    // Excludes: admin, seller, client (to avoid loading user relationships)
}