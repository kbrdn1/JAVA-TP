package edu.fbansept.m2i2.view;

/**
 * Public view for entities designed for external and unauthenticated access.
 * Extends BasicView and is specifically optimized for public-facing endpoints.
 * This view provides only information that is safe for public consumption,
 * excluding any sensitive data, internal business information, or private relationships.
 * 
 * Fields included:
 * - Only publicly safe basic information
 * 
 * This view is designed for:
 * - Public API endpoints
 * - Unauthenticated access
 * - External integrations
 * - Customer-facing interfaces
 * - Public data feeds
 * - Open API responses
 * 
 * Security considerations:
 * - Excludes all sensitive information
 * - Safe for external API consumption
 * - No internal business data exposed
 * - No user relationship information
 * - No administrative data
 * 
 * Use cases:
 * - Public REST API endpoints
 * - External partner integrations
 * - Customer-facing web services
 * - Public data exports
 * - Third-party API responses
 * - Open data initiatives
 * 
 * Performance optimized for:
 * - High-volume public access
 * - External API consumption
 * - Cacheable public data
 * - Wide distribution
 * - Public web services
 */
public class PublicView extends BasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(PublicView.class) will include:
    // - Only basic, publicly safe information
    // 
    // Specifically designed for public access with maximum security
    // Excludes: all sensitive data, user relationships, internal business information
    // Perfect for public APIs and external integrations where security is paramount
}