package com.jester.backendserver.controller;

import com.jester.backendserver.model.User;
import com.jester.backendserver.repository.UserProcedureRepository;
import com.jester.backendserver.repository.UserRepository;
import com.jester.backendserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestParam String username, @RequestParam String password) {
        Optional<User> newUser =  userService.registerUser(username, password);
        if (newUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser.get());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = userProcedureRepository.authenticateUser(username, password);

        if (isAuthenticated) {
            return ResponseEntity.ok("Authentication Successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
