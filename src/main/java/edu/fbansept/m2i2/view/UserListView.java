package edu.fbansept.m2i2.view;

/**
 * List view for User entities.
 * Extends UserBasicView and is specifically optimized for displaying users in lists.
 * This view provides the minimal necessary information for user list displays
 * while maintaining optimal performance for large datasets.
 * 
 * Fields included:
 * - All UserBasicView fields (id, email)
 * 
 * This view is designed for:
 * - User selection dropdowns
 * - User list tables
 * - Quick user references
 * - Paginated user lists
 * 
 * Performance optimized for:
 * - Minimal payload size
 * - Fast serialization
 * - Reduced network overhead
 */
public class UserListView extends UserBasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(UserListView.class) will include:
    // - User ID (inherited from UserBasicView)
    // - User email (inherited from UserBasicView)
    // 
    // Optimized for list displays with minimal data transfer
    // Excludes: password, role, products (for maximum performance)
}