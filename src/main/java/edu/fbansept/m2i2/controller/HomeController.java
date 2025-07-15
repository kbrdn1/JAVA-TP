package edu.fbansept.m2i2.controller;

import edu.fbansept.m2i2.annotation.MeasureTime;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping("/")
  @MeasureTime(message = "Retrieving API information")
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
