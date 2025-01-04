package com.jester.backendserver.controller;

import com.jester.backendserver.model.Marketplace;
import com.jester.backendserver.model.Persona;
import com.jester.backendserver.service.MarketplaceService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marketplace")
public class MarketPlaceController {

    @Autowired
    MarketplaceService marketplaceService;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MarketPlaceController.class);

    @PostMapping
    public ResponseEntity<?> getMarketplaceByUsername(@Valid @RequestBody Map<String, Object> request) {
        String logMessage = "POST /api/marketplace";

        String username = request.get("username").toString();
        log.info(logMessage + " username: " + username);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'username' is required.");
        }
        //List<Persona> personas = .getPersonasByUsername(username);
        List<Marketplace> marketplace = marketplaceService.getMarketplace(username);
        return ResponseEntity.ok(marketplace);
    }


    @PostMapping("/sellPersona")
    public ResponseEntity<?> sellPersona(@Valid @RequestBody Map<String, Object> request) {

        String logMessage = "POST /api/marketplace";

        String username = request.get("username").toString();
        String personaName = request.get("personaName").toString();
        log.info(logMessage + " username: " + username + " persona name: " + personaName);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'username' is required.");
        }
        //List<Persona> personas = .getPersonasByUsername(username);
        marketplaceService.sellPersona(username, personaName);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaName);
    }

    @PostMapping("/buyPersona")
    public ResponseEntity<?> buyPersona(@Valid @RequestBody Map<String, Object> request) {

        String logMessage = "POST /api/marketplace";

        String username = request.get("username").toString();
        String personaName = request.get("personaName").toString();
        log.info(logMessage + " username: " + username + " persona name: " + personaName);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'username' is required.");
        }
        //List<Persona> personas = .getPersonasByUsername(username);
        marketplaceService.buyPersona(username, personaName);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaName);
    }

    @PostMapping("/sellerpersonas")
    public ResponseEntity<?> getSellerPersonas(@Valid @RequestBody Map<String, Object> request) {

        String logMessage = "POST /api/marketplace";

        String username = request.get("username").toString();
        log.info(logMessage + " username: " + username);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'username' is required.");
        }

        List<Marketplace> marketplace = marketplaceService.getSellerPersonas(username);
        return ResponseEntity.ok(marketplace);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromMarketplace(@Valid @RequestBody Map<String, Object> request) {

        String logMessage = "POST /api/marketplace";

        String username = request.get("username").toString();
        String personaName = request.get("personaName").toString();
        log.info(logMessage + " username: " + username + " persona name: " + personaName);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'username' is required.");
        }
        //List<Persona> personas = .getPersonasByUsername(username);
        marketplaceService.removeFromMarketplace(username, personaName);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaName);
    }


}
