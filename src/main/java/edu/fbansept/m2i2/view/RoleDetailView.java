package edu.fbansept.m2i2.view;

/**
 * Detail view for Role entities.
 * Extends RoleWithUsersView to include comprehensive role information including detailed user relationships.
 * This view is optimal for role administration pages and complete role profiles.
 * 
 * Fields included:
 * - All RoleWithUsersView fields (id, name, users)
 * - Additional detailed role information if added in the future
 * 
 * This view is designed for:
 * - Role administration dashboards
 * - Complete role detail pages
 * - Role management interfaces with full context
 * - Administrative role views with detailed user information
 * 
 * Use cases:
 * - Full role management interfaces
 * - Administrative role detail displays
 * - Role analysis and reporting
 * - Complete role-user relationship views
 * 
 * Performance considerations:
 * - This view includes full user relationships
 * - Use with caution for roles with many users
 * - Consider pagination for large user lists
 */
public class RoleDetailView extends RoleWithUsersView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(RoleDetailView.class) will include:
    // - Role ID (inherited from RoleBasicView)
    // - Role name (inherited from RoleBasicView)
    // - Users list (inherited from RoleWithUsersView)
    // - Any additional detail fields added to Role entity in the future
    //
    // This provides the most comprehensive view of role information
    // including all relationships and detailed data
}