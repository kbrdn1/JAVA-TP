package edu.fbansept.m2i2.view;

/**
 * Summary view for Product entities.
 * Extends ProductBasicView to include basic product information plus user relationship information.
 * This view is optimal for product management pages and detailed product listings with user context.
 * 
 * Fields included:
 * - All ProductBasicView fields (id, name, price, description, stock)
 * - Admin user information
 * - Seller user information
 * - Client user information (if assigned)
 * 
 * This view is designed for:
 * - Product management interfaces
 * - Administrative product listings
 * - Business relationship displays
 * - Product detail pages with user context
 * 
 * Use cases:
 * - Viewing product information with responsible users
 * - Product management dashboards
 * - Business constraint validation displays
 * - Product-user relationship views
 * 
 * Business context:
 * - Shows which admin manages the product
 * - Shows which seller is responsible for sales
 * - Shows which client has purchased the product (if any)
 * - Useful for business workflow tracking
 */
public class ProductSummaryView extends ProductBasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(ProductSummaryView.class) will include:
    // - Product ID (inherited from ProductBasicView)
    // - Product name (inherited from ProductBasicView)
    // - Product price (inherited from ProductBasicView)
    // - Product description (inherited from ProductBasicView)
    // - Product stock (inherited from ProductBasicView)
    // - Admin user information
    // - Seller user information
    // - Client user information (if assigned)
    //
    // Note: User information will be serialized according to their own view annotations
    // typically showing basic user information to maintain reasonable payload size
}