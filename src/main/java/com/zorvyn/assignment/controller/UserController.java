package com.zorvyn.assignment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.zorvyn.assignment.dto.CreateUserRequestDTO;
import com.zorvyn.assignment.dto.CreateUserResponseDTO;
import com.zorvyn.assignment.entity.User;
import com.zorvyn.assignment.service.UserService;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService ) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    
    @PostMapping
public ResponseEntity<CreateUserResponseDTO> create(@RequestBody CreateUserRequestDTO request) {
    CreateUserResponseDTO response = userService.createUser(request);
    return ResponseEntity.status(201).body(response);
}

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id,
                                              @RequestParam boolean active) {
        userService.setUserActiveStatus(id, active);
        return ResponseEntity.ok().build();
    }
}