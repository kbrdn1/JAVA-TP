package edu.fbansept.m2i2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.m2i2.annotation.MeasureTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import edu.fbansept.m2i2.view.RoleBasicView;
import edu.fbansept.m2i2.view.RoleWithUsersView;
import edu.fbansept.m2i2.view.UserSummaryView;
import edu.fbansept.m2i2.dao.RoleDao;
import edu.fbansept.m2i2.dao.UserDao;
import edu.fbansept.m2i2.model.Role;
import edu.fbansept.m2i2.model.User;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "🎭 Role Management", description = "Role definition and user assignment management with JsonView optimization")
public class RoleController {

  @Autowired
  protected RoleDao roleDao;

  @Autowired
  protected UserDao userDao;

  @GetMapping
  @MeasureTime(message = "Retrieving all roles")
  @JsonView(RoleBasicView.class)
  @Operation(
    summary = "Get all roles (Basic View)",
    description = "Retrieves all roles with basic information using RoleBasicView. " +
                  "Perfect for role selection dropdowns and basic role lists. " +
                  "Payload: ~40 bytes per role. Includes only ID and name.",
    tags = {"🎭 Role Management", "⚡ Performance Optimized"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Roles retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Role.class),
        examples = @ExampleObject(
          name = "RoleBasicView Example",
          value = "[{\"id\": 1, \"name\": \"CLIENT\"}, {\"id\": 2, \"name\": \"ADMIN\"}, {\"id\": 3, \"name\": \"SELLER\"}]"
        )
      )
    ),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  public List<Role> getAll() {
    return roleDao.findAll();
  }

  @GetMapping("/{id}")
  @MeasureTime(message = "Retrieving role by ID", includeParameters = true)
  @JsonView(RoleWithUsersView.class)
  @Operation(
    summary = "Get role by ID (With Users View)",
    description = "Retrieves a specific role with associated users using RoleWithUsersView. " +
                  "Perfect for role management interfaces and user-role relationship displays. " +
                  "Payload: ~200-1000 bytes depending on user count.",
    tags = {"🎭 Role Management", "👥 User Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Role found and returned with associated users",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Role.class),
        examples = @ExampleObject(
          name = "RoleWithUsersView Example",
          value = "{\"id\": 1, \"name\": \"CLIENT\", \"users\": [{\"id\": 1, \"email\": \"john.client@example.com\"}, {\"id\": 4, \"email\": \"sarah.client@example.com\"}]}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Role not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "id", description = "Role ID", required = true, example = "1")
  public ResponseEntity<Role> get(@PathVariable int id) {
    Optional<Role> roleOptional = roleDao.findById(id);

    if (roleOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(roleOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/{roleId}/users")
  @MeasureTime(message = "Retrieving users by role ID", includeParameters = true)
  @JsonView(UserSummaryView.class)
  @Operation(
    summary = "Get users by role ID",
    description = "Retrieves all users assigned to a specific role using UserSummaryView. " +
                  "Returns users with their role information for role-based user management. " +
                  "Payload: ~120 bytes per user.",
    tags = {"🎭 Role Management", "👥 User Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Users with specified role retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
          name = "Users by Role Example",
          value = "[{\"id\": 1, \"email\": \"john.client@example.com\", \"role\": {\"id\": 1, \"name\": \"CLIENT\"}}]"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Role not found"),
    @ApiResponse(responseCode = "409", description = "Conflict error")
  })
  @Parameter(name = "roleId", description = "Role ID to get users for", required = true, example = "1")
  public ResponseEntity<List<User>> getUsersByRoleId(@PathVariable int roleId) {
    Optional<Role> roleOptional = roleDao.findById(roleId);

    if (roleOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<User> roleUsers = userDao.findByRole(roleOptional.get());
    return new ResponseEntity<>(roleUsers, HttpStatus.OK);
  }

  @PostMapping
  @MeasureTime(message = "Creating a new role", logLevel = "DEBUG")
  @JsonView(RoleBasicView.class)
  @Operation(
    summary = "Create a new role",
    description = "Creates a new role in the system. Role name must be unique. " +
                  "Supports CLIENT, ADMIN, and SELLER roles with business constraint validation. " +
                  "Returns the created role in RoleBasicView format.",
    tags = {"🎭 Role Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Role created successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Role.class),
        examples = @ExampleObject(
          name = "Created Role Example",
          value = "{\"id\": 4, \"name\": \"MANAGER\"}"
        )
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid role data"),
    @ApiResponse(responseCode = "409", description = "Role name already exists")
  })
  public ResponseEntity<Role> add(
    @Parameter(description = "Role data to create", required = true)
    @RequestBody @Validated(Role.add.class) Role roleSent
  ) {
    roleDao.save(roleSent);

    return new ResponseEntity<>(roleSent, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @MeasureTime(message = "Deleting role", includeParameters = true, logLevel = "WARN")
  @Operation(
    summary = "Delete role by ID",
    description = "Deletes a role from the system. ⚠️ WARNING: This will cascade delete all associated users! " +
                  "Use with extreme caution in production. Consider deactivating roles instead.",
    tags = {"🎭 Role Management", "🛡️ Security Features"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "204",
      description = "Role deleted successfully"
    ),
    @ApiResponse(responseCode = "404", description = "Role not found"),
    @ApiResponse(responseCode = "409", description = "Conflict - role might have dependent users")
  })
  @Parameter(name = "id", description = "Role ID to delete", required = true, example = "4")
  public ResponseEntity<?> delete(@PathVariable int id) {
    Optional<Role> roleOptional = roleDao.findById(id);

    if (roleOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    roleDao.deleteById(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/{id}")
  @MeasureTime(message = "Updating role", includeParameters = true)
  @JsonView(RoleBasicView.class)
  @Operation(
    summary = "Update role by ID",
    description = "Updates an existing role's information. Role name must remain unique. " +
                  "Changes to role names will affect all associated users. " +
                  "Returns the updated role in RoleBasicView format.",
    tags = {"🎭 Role Management"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Role updated successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Role.class),
        examples = @ExampleObject(
          name = "Updated Role Example",
          value = "{\"id\": 1, \"name\": \"PREMIUM_CLIENT\"}"
        )
      )
    ),
    @ApiResponse(responseCode = "404", description = "Role not found"),
    @ApiResponse(responseCode = "400", description = "Invalid role data"),
    @ApiResponse(responseCode = "409", description = "Role name already exists")
  })
  public ResponseEntity<?> update(
    @Parameter(description = "Role ID to update", required = true, example = "1")
    @PathVariable int id,
    @Parameter(description = "Updated role data", required = true)
    @RequestBody @Validated(Role.update.class) Role roleSent
  ) {
    roleSent.setId(id);

    Optional<Role> roleOptional = roleDao.findById(id);

    if (roleOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    roleDao.save(roleSent);

    return new ResponseEntity<>(roleSent, HttpStatus.OK);
  }
}
