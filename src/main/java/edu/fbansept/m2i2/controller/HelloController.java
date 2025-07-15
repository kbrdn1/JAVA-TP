package edu.fbansept.m2i2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "👋 Hello World", description = "Simple greeting endpoint for API testing and connectivity verification")
public class HelloController {

  @GetMapping("/hello")
  @Operation(
    summary = "Simple Hello World greeting",
    description = "Returns a simple 'Hello' greeting message. " +
                  "Perfect for testing API connectivity, health checks, and basic endpoint verification. " +
                  "🌟 Lightweight endpoint with minimal processing overhead.",
    tags = {"👋 Hello World", "🔧 Testing & Utilities"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Hello message returned successfully",
      content = @Content(
        mediaType = "text/plain",
        examples = @ExampleObject(
          name = "Hello Response",
          value = "Hello"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public String hello() {
    return "Hello";
  }
}
