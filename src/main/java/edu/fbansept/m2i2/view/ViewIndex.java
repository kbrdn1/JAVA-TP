package edu.fbansept.m2i2.view;

/**
 * ViewIndex - Comprehensive reference for all JsonView classes in the application.
 * 
 * This class serves as a central documentation and quick reference for all view classes
 * used in the M2I2 application. It provides information about view hierarchy,
 * usage patterns, and performance considerations.
 * 
 * VIEW HIERARCHY:
 * 
 * Base Views:
 * - BasicView (foundation)
 *   └── SummaryView (extends BasicView)
 *       └── DetailView (extends SummaryView)
 * - PublicView (extends BasicView, for external access)
 * 
 * User Views:
 * - UserBasicView (extends BasicView)
 *   ├── UserListView (extends UserBasicView, optimized for lists)
 *   └── UserSummaryView (extends UserBasicView)
 *       └── UserDetailView (extends UserSummaryView)
 * 
 * Role Views:
 * - RoleBasicView (extends BasicView)
 *   └── RoleWithUsersView (extends RoleBasicView)
 *       └── RoleDetailView (extends RoleWithUsersView)
 * 
 * Product Views:
 * - ProductBasicView (extends BasicView)
 *   ├── ProductListView (extends ProductBasicView, optimized for lists)
 *   ├── ProductCatalogView (extends ProductBasicView, for public access)
 *   └── ProductSummaryView (extends ProductBasicView)
 *       └── ProductDetailView (extends ProductSummaryView)
 * 
 * @author M2I2 Development Team
 * @version 1.0
 * @since 2024
 */
public final class ViewIndex {
    
    private ViewIndex() {
        // Utility class - prevent instantiation
    }
    
    /**
     * User view documentation and quick reference.
     */
    public static final class User {
        
        /**
         * UserBasicView - Minimal user information
         * Fields: id, email
         * Use for: User dropdowns, basic references, minimal lists
         * Payload: ~50 bytes per user
         * Performance: Excellent - minimal data transfer
         */
        public static final Class<?> BASIC = UserBasicView.class;
        
        /**
         * UserListView - Optimized for user lists
         * Fields: id, email (same as basic but semantically different)
         * Use for: User management lists, selection interfaces
         * Payload: ~50 bytes per user
         * Performance: Excellent - optimized for large lists
         */
        public static final Class<?> LIST = UserListView.class;
        
        /**
         * UserSummaryView - User with role information
         * Fields: id, email, role (id, name)
         * Use for: User cards, user lists with context, management interfaces
         * Payload: ~120 bytes per user
         * Performance: Good - includes role relationship
         */
        public static final Class<?> SUMMARY = UserSummaryView.class;
        
        /**
         * UserDetailView - Complete user information
         * Fields: id, email, role, adminProducts, sellerProducts, clientProducts
         * Use for: User detail pages, full user profiles, edit forms
         * Payload: ~500-2000 bytes (depends on product relationships)
         * Performance: Heavy - includes all product relationships
         */
        public static final Class<?> DETAIL = UserDetailView.class;
        
        private User() {}
    }
    
    /**
     * Role view documentation and quick reference.
     */
    public static final class Role {
        
        /**
         * RoleBasicView - Minimal role information
         * Fields: id, name
         * Use for: Role dropdowns, role selection, role references
         * Payload: ~40 bytes per role
         * Performance: Excellent - minimal data transfer
         */
        public static final Class<?> BASIC = RoleBasicView.class;
        
        /**
         * RoleWithUsersView - Role with associated users
         * Fields: id, name, users (basic user info)
         * Use for: Role management, user-role relationship displays
         * Payload: ~200-1000 bytes (depends on user count)
         * Performance: Moderate - includes user relationships
         */
        public static final Class<?> WITH_USERS = RoleWithUsersView.class;
        
        /**
         * RoleDetailView - Complete role information
         * Fields: id, name, users (complete user info)
         * Use for: Role administration, detailed role management
         * Payload: ~500-5000 bytes (depends on users and their relationships)
         * Performance: Heavy - includes detailed user relationships
         */
        public static final Class<?> DETAIL = RoleDetailView.class;
        
        private Role() {}
    }
    
    /**
     * Product view documentation and quick reference.
     */
    public static final class Product {
        
        /**
         * ProductBasicView - Core product information
         * Fields: id, name, price, description, stock
         * Use for: Internal product lists, admin interfaces
         * Payload: ~150 bytes per product
         * Performance: Good - no relationship data
         */
        public static final Class<?> BASIC = ProductBasicView.class;
        
        /**
         * ProductListView - Optimized for product lists
         * Fields: id, name, price, description, stock (same as basic but semantically different)
         * Use for: Product management lists, inventory displays
         * Payload: ~150 bytes per product
         * Performance: Excellent - optimized for large lists
         */
        public static final Class<?> LIST = ProductListView.class;
        
        /**
         * ProductCatalogView - Public product catalog
         * Fields: id, name, price, description, stock (same as basic but for public access)
         * Use for: Public catalogs, customer-facing displays, external APIs
         * Payload: ~150 bytes per product
         * Performance: Excellent - safe for public access, cacheable
         */
        public static final Class<?> CATALOG = ProductCatalogView.class;
        
        /**
         * ProductSummaryView - Product with user relationships
         * Fields: id, name, price, description, stock, admin, seller, client
         * Use for: Product management, business relationship displays
         * Payload: ~350-500 bytes per product
         * Performance: Moderate - includes user relationship data
         */
        public static final Class<?> SUMMARY = ProductSummaryView.class;
        
        /**
         * ProductDetailView - Complete product information
         * Fields: id, name, price, description, stock, admin, seller, client (with full user details)
         * Use for: Product detail pages, product editing, full product management
         * Payload: ~800-1500 bytes per product
         * Performance: Heavy - includes detailed user relationships
         */
        public static final Class<?> DETAIL = ProductDetailView.class;
        
        private Product() {}
    }
    
    /**
     * Generic view documentation and quick reference.
     */
    public static final class Generic {
        
        /**
         * BasicView - Foundation view for all entities
         * Use for: Custom view inheritance, minimal entity data
         * Performance: Variable - depends on entity implementation
         */
        public static final Class<?> BASIC = BasicView.class;
        
        /**
         * SummaryView - Enhanced basic view with relationships
         * Use for: Custom view inheritance, moderate entity data
         * Performance: Variable - depends on entity implementation
         */
        public static final Class<?> SUMMARY = SummaryView.class;
        
        /**
         * DetailView - Comprehensive view with all relationships
         * Use for: Custom view inheritance, complete entity data
         * Performance: Variable - depends on entity implementation
         */
        public static final Class<?> DETAIL = DetailView.class;
        
        /**
         * PublicView - Safe view for external access
         * Use for: Public APIs, external integrations, unauthenticated access
         * Performance: Excellent - minimal, safe data transfer
         */
        public static final Class<?> PUBLIC = PublicView.class;
        
        private Generic() {}
    }
    
    /**
     * View selection guidelines and best practices.
     */
    public static final class Guidelines {
        
        /**
         * Performance recommendations based on use case:
         * 
         * FOR LISTS (>10 items):
         * - Use LIST views (UserListView, ProductListView)
         * - Avoid DETAIL views
         * - Consider pagination for large datasets
         * 
         * FOR DROPDOWNS/SELECTIONS:
         * - Use BASIC views (UserBasicView, RoleBasicView, ProductBasicView)
         * - Minimal payload for fast rendering
         * 
         * FOR CARDS/SUMMARIES:
         * - Use SUMMARY views (UserSummaryView, ProductSummaryView)
         * - Good balance of information and performance
         * 
         * FOR DETAIL PAGES:
         * - Use DETAIL views (UserDetailView, ProductDetailView)
         * - Complete information for single entities
         * 
         * FOR PUBLIC APIs:
         * - Use CATALOG/PUBLIC views (ProductCatalogView, PublicView)
         * - Safe for external consumption
         * 
         * FOR ADMIN INTERFACES:
         * - Use WITH_USERS views for roles (RoleWithUsersView)
         * - Use SUMMARY views for products with business context
         */
        
        /**
         * Security considerations:
         * 
         * SENSITIVE DATA:
         * - Password fields have NO @JsonView annotation (never serialized)
         * - Use restrictive views for public endpoints
         * - Avoid DETAIL views for unauthenticated access
         * 
         * BUSINESS DATA:
         * - Product user relationships excluded from CATALOG views
         * - Role user relationships excluded from BASIC views
         * - Consider data sensitivity when choosing views
         */
        
        /**
         * Payload size estimates (approximate):
         * 
         * USERS:
         * - Basic/List: ~50 bytes
         * - Summary: ~120 bytes
         * - Detail: ~500-2000 bytes
         * 
         * ROLES:
         * - Basic: ~40 bytes
         * - WithUsers: ~200-1000 bytes
         * - Detail: ~500-5000 bytes
         * 
         * PRODUCTS:
         * - Basic/List/Catalog: ~150 bytes
         * - Summary: ~350-500 bytes
         * - Detail: ~800-1500 bytes
         */
        
        private Guidelines() {}
    }
    
    /**
     * Migration information for developers updating from previous implementations.
     */
    public static final class Migration {
        
        /**
         * Mapping from old JsonViews nested classes to new individual classes:
         * 
         * OLD -> NEW
         * JsonViews.User.Basic -> UserBasicView
         * JsonViews.User.Summary -> UserSummaryView
         * JsonViews.User.Detail -> UserDetailView
         * JsonViews.User.List -> UserListView
         * 
         * JsonViews.Role.Basic -> RoleBasicView
         * JsonViews.Role.WithUsers -> RoleWithUsersView
         * JsonViews.Role.Detail -> RoleDetailView
         * 
         * JsonViews.Product.Basic -> ProductBasicView
         * JsonViews.Product.Summary -> ProductSummaryView
         * JsonViews.Product.Detail -> ProductDetailView
         * JsonViews.Product.List -> ProductListView
         * JsonViews.Product.Catalog -> ProductCatalogView
         * 
         * JsonViews.Basic -> BasicView
         * JsonViews.Summary -> SummaryView
         * JsonViews.Detail -> DetailView
         * JsonViews.Public.Basic -> PublicView
         */
        
        /**
         * Benefits of new class-per-view approach:
         * 
         * 1. CLARITY: Each view is a separate class with clear purpose
         * 2. INHERITANCE: Proper OOP inheritance relationships
         * 3. DOCUMENTATION: Each class can have detailed JavaDoc
         * 4. MAINTAINABILITY: Easier to modify and extend individual views
         * 5. TOOLING: Better IDE support and refactoring capabilities
         * 6. PERFORMANCE: Jackson can optimize view handling
         * 7. TESTING: Easier to test individual view behaviors
         */
        
        private Migration() {}
    }
}