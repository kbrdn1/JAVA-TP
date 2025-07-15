package edu.fbansept.m2i2.controller;

import edu.fbansept.m2i2.annotation.MeasureTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "🏠 API Information", description = "API status, health checks, and general information endpoints")
public class HomeController {

  @GetMapping("/")
  @MeasureTime(message = "Retrieving API information")
  @Operation(
    summary = "Get API Information and Status",
    description = "Returns comprehensive API information including version, status, available endpoints, and timestamp. " +
                  "Perfect for API discovery, health checks, and integration verification. " +
                  "🏠 Home endpoint providing complete API overview and navigation.",
    tags = {"🏠 API Information"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "API information retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Map.class),
        examples = @ExampleObject(
          name = "API Information Example",
          value = "{\"message\": \"Welcome to M2I2 API\", \"version\": \"1.0.0\", \"status\": \"online\", \"timestamp\": \"2024-01-15T10:30:00\", \"endpoints\": {\"users\": \"/api/users\", \"roles\": \"/api/roles\", \"products\": \"/api/products\"}}"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public Map<String, Object> home() {
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Welcome to M2I2 API");
    response.put("version", "1.0.0");
    response.put("timestamp", LocalDateTime.now());
    response.put(
      "endpoints",
      Map.of("users", "/api/users", "roles", "/api/roles", "products", "/api/products")
    );
    response.put("status", "online");
    return response;
  }
}
