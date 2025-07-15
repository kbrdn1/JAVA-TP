package edu.fbansept.m2i2.view;

/**
 * Basic view for Role entities.
 * Includes only essential role information: id and name.
 * Excludes user relationships for optimal performance in role lists and references.
 * This view is optimal for role dropdowns, role selection, and basic role references.
 * 
 * Fields included:
 * - Role ID
 * - Role name
 * 
 * Excludes: users list (for performance optimization)
 */
public class RoleBasicView extends BasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(RoleBasicView.class) will include:
    // - Role ID
    // - Role name
    // Excludes: users (to avoid loading large user collections)
}