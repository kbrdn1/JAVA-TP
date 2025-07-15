package edu.fbansept.m2i2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for M2I2 API documentation.
 * 
 * This configuration provides comprehensive documentation for the JsonView-enhanced API,
 * including detailed information about different view levels, performance characteristics,
 * and business constraints.
 * 
 * Features documented:
 * - JsonView hierarchy and inheritance
 * - Performance-optimized endpoints
 * - Security considerations
 * - Business constraint validation
 * - Role-based access patterns
 * 
 * @author M2I2 Development Team
 * @version 2.0.0
 * @since 2024
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(createServers())
                .tags(createTags());
    }

    private Info createApiInfo() {
        return new Info()
                .title("M2I2 API - JsonView Enhanced")
                .version("2.0.0")
                .description(createApiDescription())
                .contact(createContact())
                .license(createLicense());
    }

    private String createApiDescription() {
        return """
                ## 🚀 M2I2 API with Advanced JsonView Implementation
                
                A modern Spring Boot REST API featuring **comprehensive JsonView implementation** for optimal performance and security.
                
                ### 🎯 Key Features
                - **Hierarchical JsonViews**: Basic → Summary → Detail inheritance
                - **Performance Optimized**: 60-95% payload reduction for list operations  
                - **Security Enhanced**: Automatic password exclusion and granular access control
                - **Business Constraints**: Strict role-based product management (Admin + Seller required)
                - **Circular Reference Free**: No more @JsonBackReference/@JsonManagedReference complexity
                
                ### 📊 JsonView Hierarchy
                ```
                BasicView (foundation)
                ├── UserBasicView → UserListView / UserSummaryView → UserDetailView
                ├── RoleBasicView → RoleWithUsersView → RoleDetailView  
                ├── ProductBasicView → ProductListView / ProductCatalogView / ProductSummaryView → ProductDetailView
                └── PublicView (external access)
                ```
                
                ### ⚡ Performance Guidelines
                - **Basic/List Views**: ~50-150 bytes per entity (optimal for lists)
                - **Summary Views**: ~120-500 bytes per entity (good for cards/previews)
                - **Detail Views**: ~500-2000 bytes per entity (use for single entities only)
                - **Catalog Views**: Public-safe, no business relationships exposed
                
                ### 🛡️ Security Features
                - **Password Protection**: Never serialized (no @JsonView annotation)
                - **Business Data Protection**: Internal relationships hidden in public views
                - **Role-based Access**: Different data visibility per user role
                - **Audit Trail**: All operations monitored with @MeasureTime
                
                ### 🏢 Business Rules
                - **Products MUST have**: Admin (ADMIN role) + Seller (SELLER role)
                - **Products CAN have**: Client (CLIENT role) for purchases
                - **Role Validation**: Strictly enforced for all operations
                - **Business Workflows**: Purchase flow with availability tracking
                
                ### 📚 Usage Examples
                ```bash
                # Light payload for lists
                GET /api/users/basic          # UserBasicView: ~50 bytes/user
                GET /api/products/catalog     # ProductCatalogView: ~150 bytes/product
                
                # Balanced for cards/previews  
                GET /api/users/summary        # UserSummaryView: ~120 bytes/user
                GET /api/products/with-users  # ProductSummaryView: ~350 bytes/product
                
                # Complete for detail pages
                GET /api/users/{id}           # UserDetailView: ~500-2000 bytes
                GET /api/products/entity/{id}/detail # ProductDetailView: ~800-1500 bytes
                ```
                
                ### 🔧 Development Notes
                - Use **List/Basic views** for high-volume endpoints
                - Use **Summary views** for balanced information/performance
                - Use **Detail views** only for single-entity endpoints
                - Use **Catalog/Public views** for external API access
                
                ---
                *For complete JsonView documentation, see the ViewIndex.java class and JsonView-Quick-Reference.md*
                """;
    }

    private Contact createContact() {
        return new Contact()
                .name("M2I2 Development Team")
                .email("dev@m2i2.edu")
                .url("https://github.com/m2i2/api");
    }

    private License createLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    private List<Server> createServers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Development Server"),
                new Server()
                        .url("https://api.m2i2.edu")
                        .description("Production Server (when available)")
        );
    }

    private List<Tag> createTags() {
        return List.of(
                // Main entity management tags
                new Tag()
                        .name("👥 User Management")
                        .description("Complete user lifecycle management with role-based access control. " +
                                "Includes CRUD operations and relationship management with products."),
                
                new Tag()
                        .name("🎭 Role Management") 
                        .description("Role definition and user assignment management. " +
                                "Supports CLIENT, ADMIN, and SELLER roles with business constraint validation."),
                
                new Tag()
                        .name("📦 Product Management")
                        .description("Product lifecycle with strict business constraints. " +
                                "Products MUST have Admin + Seller, CAN have Client. Full business workflow support."),
                
                // JsonView-specific tags
                new Tag()
                        .name("🔄 User JsonViews")
                        .description("JsonView-powered user endpoints with different data detail levels. " +
                                "Performance optimized: Basic (~50 bytes) → Summary (~120 bytes) → Detail (~500-2000 bytes)."),
                
                new Tag()
                        .name("🔄 Product JsonViews")
                        .description("JsonView-powered product endpoints from public catalog to detailed management views. " +
                                "Catalog (public-safe) → Basic → Summary (with users) → Detail (comprehensive)."),
                
                // Demonstration and testing tags
                new Tag()
                        .name("📊 JsonView Demonstrations")
                        .description("Live demonstrations of JsonView differences, performance comparisons, and security features. " +
                                "Perfect for understanding payload optimization and view inheritance."),
                
                new Tag()
                        .name("🏢 Business Operations")
                        .description("Business workflow endpoints including product availability, client assignment, " +
                                "role-based views, and business analytics."),
                
                new Tag()
                        .name("🏠 API Information")
                        .description("API status, health checks, and general information endpoints."),
                
                // Utility tags
                new Tag()
                        .name("🛡️ Security Features")
                        .description("Security-focused endpoints demonstrating password protection, " +
                                "public-safe views, and business data protection."),
                
                new Tag()
                        .name("⚡ Performance Optimized")
                        .description("High-performance endpoints optimized for large datasets, " +
                                "minimal payload sizes, and efficient serialization.")
        );
    }
}