package edu.fbansept.m2i2.view;

/**
 * JsonView classes for controlling JSON serialization in different contexts.
 * These views define which fields should be included when serializing entities.
 */
public class JsonViews {

    /**
     * Base views for fundamental entity information
     */
    public static class Basic {}
    
    /**
     * Summary views include basic info plus some relationship data
     */
    public static class Summary extends Basic {}
    
    /**
     * Detail views include comprehensive entity information
     */
    public static class Detail extends Summary {}

    /**
     * User-specific views
     */
    public static class User {
        /**
         * Basic user info: id, email (excludes password and relationships)
         */
        public static class Basic extends JsonViews.Basic {}
        
        /**
         * User summary: basic info + role information (excludes products)
         */
        public static class Summary extends Basic {}
        
        /**
         * User detail: full user info including role and products
         */
        public static class Detail extends Summary {}
        
        /**
         * Administrative view: includes sensitive information if needed
         */
        public static class Admin extends Detail {}
        
        /**
         * List view: optimized for displaying users in lists
         */
        public static class List extends Basic {}
    }

    /**
     * Role-specific views
     */
    public static class Role {
        /**
         * Basic role info: id, name
         */
        public static class Basic extends JsonViews.Basic {}
        
        /**
         * Role with users: includes associated users (in basic view)
         */
        public static class WithUsers extends Basic {}
        
        /**
         * Role detail: comprehensive role information
         */
        public static class Detail extends WithUsers {}
    }

    /**
     * Product-specific views
     */
    public static class Product {
        /**
         * Basic product info: id, name, price, description, stock
         */
        public static class Basic extends JsonViews.Basic {}
        
        /**
         * Product summary: basic info + basic user information for admin/seller/client
         */
        public static class Summary extends Basic {}
        
        /**
         * Product detail: full product with detailed user information
         */
        public static class Detail extends Summary {}
        
        /**
         * List view: optimized for product listings
         */
        public static class List extends Basic {}
        
        /**
         * Catalog view: public product information (excludes sensitive data)
         */
        public static class Catalog extends Basic {}
    }

    /**
     * Relationship-specific views for controlling nested entity serialization
     */
    public static class Relationship {
        /**
         * For including basic relationship info
         */
        public static class Basic extends JsonViews.Basic {}
        
        /**
         * For including summary relationship info
         */
        public static class Summary extends Basic {}
    }

    /**
     * Administrative views for management operations
     */
    public static class Admin {
        /**
         * Full administrative access view
         */
        public static class Full extends JsonViews.Detail {}
        
        /**
         * Management view for admin operations
         */
        public static class Management extends JsonViews.Summary {}
    }

    /**
     * Public views for non-authenticated access
     */
    public static class Public {
        /**
         * Public information only
         */
        public static class Basic extends JsonViews.Basic {}
    }
}