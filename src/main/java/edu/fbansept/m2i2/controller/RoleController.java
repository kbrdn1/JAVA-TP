package edu.fbansept.m2i2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.m2i2.annotation.MeasureTime;
import edu.fbansept.m2i2.view.JsonViews;
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
public class RoleController {

  @Autowired
  protected RoleDao roleDao;

  @Autowired
  protected UserDao userDao;

  @GetMapping
  @MeasureTime(message = "Retrieving all roles")
  @JsonView(JsonViews.Role.Basic.class)
  public List<Role> getAll() {
    return roleDao.findAll();
  }

  @GetMapping("/{id}")
  @MeasureTime(message = "Retrieving role by ID", includeParameters = true)
  @JsonView(JsonViews.Role.WithUsers.class)
  public ResponseEntity<Role> get(@PathVariable int id) {
    Optional<Role> roleOptional = roleDao.findById(id);

    if (roleOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(roleOptional.get(), HttpStatus.OK);
  }

  @GetMapping("/{roleId}/users")
  @MeasureTime(message = "Retrieving users by role ID", includeParameters = true)
  @JsonView(JsonViews.User.Summary.class)
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
  @JsonView(JsonViews.Role.Basic.class)
  public ResponseEntity<Role> add(@RequestBody @Validated(Role.add.class) Role roleSent) {
    roleDao.save(roleSent);

    return new ResponseEntity<>(roleSent, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @MeasureTime(message = "Deleting role", includeParameters = true, logLevel = "WARN")
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
  @JsonView(JsonViews.Role.Basic.class)
  public ResponseEntity<?> update(
    @PathVariable int id,
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
