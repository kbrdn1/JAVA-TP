package edu.fbansept.m2i2.view;

/**
 * Catalog view for Product entities designed for public access.
 * Extends ProductBasicView and is specifically optimized for public-facing product catalogs.
 * This view provides product information suitable for external consumers, customers,
 * and public API access while excluding sensitive business relationship data.
 * 
 * Fields included:
 * - All ProductBasicView fields (id, name, price, description, stock)
 * 
 * This view is designed for:
 * - Public product catalogs
 * - E-commerce websites
 * - Customer-facing product displays
 * - External API responses
 * - Product feeds for third parties
 * - Public search interfaces
 * 
 * Security considerations:
 * - Excludes all user relationship information
 * - Excludes internal business data
 * - Safe for public API endpoints
 * - No sensitive business information exposed
 * 
 * Use cases:
 * - Public e-commerce product listings
 * - Customer product browsing
 * - External partner integrations
 * - Public API product endpoints
 * - Product feeds for marketing
 * - Third-party catalog integrations
 * 
 * Performance optimized for:
 * - Public web displays
 * - External API responses
 * - Customer-facing interfaces
 * - High-volume public access
 * - Cacheable product data
 */
public class ProductCatalogView extends ProductBasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(ProductCatalogView.class) will include:
    // - Product ID (inherited from ProductBasicView)
    // - Product name (inherited from ProductBasicView)
    // - Product price (inherited from ProductBasicView)
    // - Product description (inherited from ProductBasicView)
    // - Product stock (inherited from ProductBasicView)
    // 
    // Specifically designed for public access with no sensitive business information
    // Excludes: admin, seller, client relationships (business sensitive data)
    // Perfect for customer-facing product catalogs and public API responses
}