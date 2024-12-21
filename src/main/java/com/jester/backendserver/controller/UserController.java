package com.jester.backendserver.controller;

import com.jester.backendserver.model.User;
import com.jester.backendserver.model.UserRegistrationDTO;
import com.jester.backendserver.repository.UserProcedureRepository;
import com.jester.backendserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserProcedureRepository userProcedureRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getByUsername")
    public ResponseEntity<User> getUser(@Valid @RequestParam String username) {
        try {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username doesn't exist.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDTO user) {

        if (userService.existByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.");
        }
        try {
            userService.registerUser(user.getUsername(), user.getPassword());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User created with username: " + user.getUsername());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@Valid @RequestBody UserRegistrationDTO user) {
        boolean isAuthenticated = userProcedureRepository.authenticateUser(user.getUsername(), user.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("Authentication Successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@Valid @RequestBody UserRegistrationDTO user) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Deletion is not yet implemented, our DB doesn't support it. Please contact Bilgehan Sahlan so he can implement it.");
    }
}
