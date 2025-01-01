package com.jester.backendserver.controller;

import com.jester.backendserver.model.Persona;
import com.jester.backendserver.service.PersonaService;
import com.jester.backendserver.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/personas/image")
public class PersonaImageController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PersonaImageController.class);
    private final PersonaService personaService;

    public PersonaImageController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> getTestImage() {
        byte[] imageBytes = personaService.getImageBytes(1);
        if (imageBytes == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("pid 1 has no image set");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @GetMapping
    public ResponseEntity<?> getPersonaImage(@Valid @RequestBody Map<String, Object> request) {
        try {
            // Extract Inputs
            Integer pid = (Integer) request.get("pid");
            String logMessage = "GET /api/personas/image";
            log.info(logMessage);

            // Validate Inputs
            if (pid == null || pid < 0) {
                return ResponseEntity.badRequest().body("Error: 'pid' is required.");
            }

            // Process request
            Persona persona = personaService.getPersona(pid);
            if (persona == null) {
                return ResponseEntity.badRequest().body("Error: Persona not found.");
            }

            byte [] imageBytes = personaService.getImageBytes(pid);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Error: Persona not found.");
        } catch (Exception e) {
            log.error("Error processing getPersonaImage request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }

    @PostMapping
    public ResponseEntity<?> setPersonaImageBase64(@Valid @RequestBody Map<String, Object> request) {
        try {
            // Extract inputs
            Integer pid = (Integer) request.get("pid");
            String image_b64 = (String) request.get("image");

            String logMessage = "POST /api/personas/image";
            log.info(logMessage);

            // Validate inputs
            if (pid == null || pid < 0) {
                return ResponseEntity.badRequest().body("Error: 'pid' is required.");
            }
            if (image_b64 == null || image_b64.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'image' is required.");
            }
            if (!isBase64(image_b64)){
                return ResponseEntity.badRequest().body("Error: 'image' is not a Base64 image.");
            }

            // Process request
            Persona persona = personaService.getPersona(pid);
            if (persona == null) {
                return ResponseEntity.badRequest().body("Error: Persona not found.");
            }
            personaService.setImageBase64(pid, image_b64);
            return ResponseEntity.ok(persona);

        } catch (Exception e) {
            log.error("Error processing getImageBase64 request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }

    private boolean isBase64(String base64) {
        try {
            Base64.getDecoder().decode(base64);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
