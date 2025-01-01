package com.jester.backendserver.controller;

import com.jester.backendserver.model.User;
import com.jester.backendserver.repository.UserProcedureRepository;
import com.jester.backendserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final String endpoint = "/api/users";

    private final UserProcedureRepository userProcedureRepository;
    private final UserService userService;
    private final org.slf4j.Logger log;

    public UserController(UserProcedureRepository userProcedureRepository, UserService userService) {
        this.userProcedureRepository = userProcedureRepository;
        this.userService = userService;
        this.log = LoggerFactory.getLogger(Logger.class);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        log.info("GET " + endpoint);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<User> getUserByID(@Valid @PathVariable Long uid) {
        log.info("GET " + "/api/users/id=? {}", uid);
        try {
            return ResponseEntity.ok(userService.getUser(uid));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with the given id: " + uid + " doesn't exist.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getByUsername")
    public ResponseEntity<User> getUserByUsername(@Valid @RequestParam String username) {
        log.info("GET " + endpoint + "/getByUsername? {}", username);
        try {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username doesn't exist.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody Map<String, Object> request) {
        String logMessage = "POST /register: ";
        try {
            // Extract inputs
            String username = (String) request.get("username");
            String password = (String) request.get("password");

            // Log inputs
            logMessage += "username: " + username + ", password: " + password;

            // Validate Inputs
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'username' is required.");
            }
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'password' is required.");
            }
            if (userService.existByUsername(username)) {
                return ResponseEntity.badRequest().body("Error: User already exists: " + username);
            }

            // Process the request
            User newuser = userService.registerUser(username, password);
            log.info(logMessage);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created with username: " + newuser.getUsername());
        }
        catch (Exception e) {
            log.error("Error processing register request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@Valid @RequestBody Map<String, Object> request) {
        String logMessage = "POST /authenticate: ";
        try {
            // Extract inputs
            String username = (String) request.get("username");
            String password = (String) request.get("password");

            // Log inputs
            logMessage += "username: " + username + ", password: " + password;

            // Validate Inputs
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'username' is required.");
            }
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'password' is required.");
            }

            // Process the request
            boolean isAuthenticated = userProcedureRepository.authenticateUser(username, password);
            if (isAuthenticated) {
                log.info(logMessage + "User authenticated.");
                return ResponseEntity.ok("Authentication Successful");
            }
            log.info(logMessage + "User not authenticated.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        catch (Exception e) {
            log.error("Error processing register request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@Valid @RequestBody Map<String, Object> request) {
        String logMessage = "DEL /delete: ";
        log.info(logMessage);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Deletion is not yet implemented, our DB doesn't support it. Please contact Bilgehan Sahlan so he can implement it.");
    }

}
