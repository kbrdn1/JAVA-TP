package edu.fbansept.m2i2.view;

/**
 * Detail view for User entities.
 * Extends UserSummaryView to include comprehensive user information including all product relationships.
 * This view is optimal for individual user detail pages and full user profiles.
 * 
 * Fields included:
 * - All UserSummaryView fields (id, email, role)
 * - Product relationships (adminProducts, sellerProducts, clientProducts)
 * 
 * Excludes: password (always excluded for security)
 */
public class UserDetailView extends UserSummaryView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(UserDetailView.class) will include:
    // - User ID (inherited from UserBasicView)
    // - User email (inherited from UserBasicView)
    // - User role information (inherited from UserSummaryView)
    // - Admin products list
    // - Seller products list
    // - Client products list
    // Excludes: password (never serialized for security)
}