package edu.fbansept.m2i2.view;

/**
 * Detail view for Product entities.
 * Extends ProductSummaryView to include comprehensive product information including detailed user relationships.
 * This view is optimal for individual product detail pages and complete product profiles.
 * 
 * Fields included:
 * - All ProductSummaryView fields (id, name, price, description, stock, admin, seller, client)
 * - Additional detailed product information if added in the future
 * 
 * This view is designed for:
 * - Individual product detail pages
 * - Complete product profiles
 * - Product administration interfaces
 * - Detailed product management views
 * - Product edit/update forms with full context
 * 
 * Use cases:
 * - Viewing complete product information with all relationships
 * - Product detail displays for administrators
 * - Product edit forms with full user context
 * - Detailed product analysis and reporting
 * - Complete business relationship tracking
 * 
 * Business context:
 * - Shows complete product information
 * - Includes detailed admin user information
 * - Includes detailed seller user information
 * - Includes detailed client user information (if purchased)
 * - Provides full business workflow context
 * - Useful for complete product lifecycle tracking
 * 
 * Performance considerations:
 * - This view includes full user relationships
 * - Use for single product requests, not for lists
 * - Consider the depth of user information serialization
 * - Optimal for detailed product management operations
 */
public class ProductDetailView extends ProductSummaryView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(ProductDetailView.class) will include:
    // - Product ID (inherited from ProductBasicView)
    // - Product name (inherited from ProductBasicView)
    // - Product price (inherited from ProductBasicView)
    // - Product description (inherited from ProductBasicView)
    // - Product stock (inherited from ProductBasicView)
    // - Admin user information (inherited from ProductSummaryView)
    // - Seller user information (inherited from ProductSummaryView)
    // - Client user information (inherited from ProductSummaryView)
    // - Any additional detail fields added to Product entity in the future
    //
    // This provides the most comprehensive view of product information
    // including all relationships and detailed data for complete product management
}