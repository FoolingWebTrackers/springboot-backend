package com.jester.backendserver.controller;

import com.jester.backendserver.model.Persona;
import com.jester.backendserver.service.PersonaService;
import com.jester.backendserver.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PersonaController.class);
    private final PersonaService personaService;
    private final UserService userService;

    public PersonaController(PersonaService personaService, UserService userService) {
        this.personaService = personaService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Persona>> getAllPersonas(@Valid @RequestBody Map<String, Object> request) {
        String logMessage = "GET /api/personas";
        log.info(logMessage);
        String username = request.get("username").toString();
        List<Persona> personas;
        if (username == null || username.isEmpty()) {
            personas = personaService.getPersonas();
        }
        else {
            personas = personaService.getPersonasByUsername(username);
        }


        return ResponseEntity.ok(personas);
    }

    @GetMapping("/{pid}")
    public ResponseEntity<?> getPersona(@PathVariable Integer pid) {
        String logMessage = "GET /api/personas/pid=" + pid;
        log.info(logMessage);
        if (pid == null || pid < 0) {
            return ResponseEntity.badRequest().body("Error: A positive integer is required for pid");
        }
        Persona persona = personaService.getPersona(pid);
        if (persona == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(persona);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPersona(@Valid @RequestBody Map<String, Object> request) {
        String logMessage = "POST api/personas/register: ";
        try {
            // Extract inputs
            String username = (String) request.get("username");
            String personaName = (String) request.get("personaName");
            String description = (String) request.get("description");
            Boolean generatePhoto = (Boolean) request.get("generatePhoto");

            // Log inputs
            logMessage += "username: " + username + ", personaName: " + personaName + ", description: " + description + ", generatePhoto: " + generatePhoto;

            // Validate Inputs
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'username' is required.");
            }
            if (personaName == null || personaName.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'personaName' is required.");
            }
            if (description == null || description.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'description' is required.");
            }
            if (generatePhoto == null) {
                return ResponseEntity.badRequest().body("Error: 'generatePhoto' is required.");
            }

            if (!userService.existByUsername(username)){
                return ResponseEntity.badRequest().body("Error: User not found: " + username);
            }

            // Process the request
            Persona newPersona =  personaService.registerPersona(username, personaName, description, generatePhoto);
            log.info(logMessage);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPersona);

        } catch (Exception e) {
            log.error("Error processing register request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }

    @PostMapping("/url")
    public ResponseEntity<?> addPersonaURL(@Valid @RequestBody Map<String, Object> request) {
        try {
            // Extract inputs
            Integer pid = (Integer) request.get("pid");
            List<String> urls = (List<String>) request.get("urls");

            // Log inputs
            log.info("Received pid: {}", pid);
            log.info("Received URLs: {}", urls);

            // Validate inputs
            if (pid == null || pid < 0) {
                return ResponseEntity.badRequest().body("Error: 'pid' is required.");
            }
            if (urls == null || urls.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'urls' is required.");
            }

            // Additional validation: ensure URLs are valid
            for (String url : urls) {
                if (!isValidURL(url)) {
                    return ResponseEntity.badRequest().body("Error: Invalid URL found - " + url);
                }
            }

            // Process the request
            Persona persona = personaService.getPersona(pid);
            if (persona == null) {
                return ResponseEntity.badRequest().body("Error: Persona not found.");
            }

            personaService.addURLs(urls, persona.getId());
            return ResponseEntity.ok(persona);

        } catch (Exception e) {
            log.error("Error processing addPersonaURL request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }

    // Utility method for URL validation
    private boolean isValidURL(String url) {
        try {
            new java.net.URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
