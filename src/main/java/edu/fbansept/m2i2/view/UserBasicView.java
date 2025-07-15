package edu.fbansept.m2i2.view;

/**
 * Basic view for User entities.
 * Includes only essential user information: id and email.
 * Excludes password, relationships, and other sensitive data.
 * This view is optimal for user lists and basic user references.
 */
public class UserBasicView extends BasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(UserBasicView.class) will include:
    // - User ID
    // - User email
    // Excludes: password, role, products
}