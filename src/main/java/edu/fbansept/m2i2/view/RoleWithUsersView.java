package edu.fbansept.m2i2.view;

/**
 * Role view that includes associated users.
 * Extends RoleBasicView to include basic role information plus the list of users assigned to this role.
 * This view is optimal for role management pages and detailed role information displays.
 * 
 * Fields included:
 * - All RoleBasicView fields (id, name)
 * - Associated users list
 * 
 * This view is designed for:
 * - Role detail pages
 * - Role management interfaces
 * - User-role relationship displays
 * - Administrative role views
 * 
 * Use cases:
 * - Viewing which users have a specific role
 * - Role administration and management
 * - Role detail displays with user context
 */
public class RoleWithUsersView extends RoleBasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(RoleWithUsersView.class) will include:
    // - Role ID (inherited from RoleBasicView)
    // - Role name (inherited from RoleBasicView)  
    // - Users list (associated users with this role)
    //
    // Note: Users in the list will be serialized according to their own view annotations
    // typically showing basic user information to avoid deep nesting
}