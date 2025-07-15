package edu.fbansept.m2i2.view;

/**
 * Summary view for User entities.
 * Extends UserBasicView to include basic user information plus role information.
 * This view is optimal for user lists with role context and user cards.
 * 
 * Fields included:
 * - All UserBasicView fields (id, email)
 * - User role information
 * 
 * Excludes: password, product relationships
 */
public class UserSummaryView extends UserBasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(UserSummaryView.class) will include:
    // - User ID (inherited from UserBasicView)
    // - User email (inherited from UserBasicView)
    // - User role information
    // Excludes: password, adminProducts, sellerProducts, clientProducts
}