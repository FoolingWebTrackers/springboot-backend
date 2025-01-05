package com.jester.backendserver.controller;

import com.jester.backendserver.model.Marketplace;
import com.jester.backendserver.model.Persona;
import com.jester.backendserver.model.User;
import com.jester.backendserver.service.MarketplaceService;
import com.jester.backendserver.service.PersonaService;
import com.jester.backendserver.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/marketplace")
public class MarketPlaceController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MarketPlaceController.class);

    @Autowired
    MarketplaceService marketplaceService;
    @Autowired
    UserService userService;
    @Autowired
    private PersonaService personaService;


    @PostMapping("/items")
    public ResponseEntity<?> listMarketplaceItems(@Valid @RequestBody Map<String, Object> request) {
        String logMessage = "POST /api/marketplace";

        String username = request.get("username").toString();
        log.info(logMessage + " username: " + username);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'username' is required.");
        }
        if (!userService.existByUsername(username)) {
            return ResponseEntity.badRequest().body("Error: This user does not exist: " + username);
        }

        List<Marketplace> marketplace = marketplaceService.getMarketplace(username);
        return ResponseEntity.ok(marketplace);
    }


    @PostMapping("/sell-persona")
    public ResponseEntity<?> sellPersona(@Valid @RequestBody Map<String, Object> request) {
        String logMessage = "POST /api/marketplace/sellPersona";

        String username = request.get("username").toString();
        String personaName = request.get("personaName").toString();

        log.info(logMessage + " username: " + username + " personaName: " + personaName);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'username' is required.");
        }
        else if (personaName == null || personaName.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'personaName' is required.");
        }

        Persona persona;
        User user;
        try{
            user = userService.getUserByUsername(username);
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity.badRequest().body("Error: user with username: " + " does not exist.");
        }

        List<Persona> user_personas = user.getPersonas();
        List<String> user_persona_names = user_personas.stream().map(Persona::getName).toList();
        if(!user_persona_names.contains(personaName)){
            return ResponseEntity.badRequest().body("Error: persona: " + personaName + " is not owned by user: " + username);
        }


        marketplaceService.sellPersona(username, personaName);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaName);
    }

    @PostMapping("/buy-persona")
    public ResponseEntity<?> buyPersona(@Valid @RequestBody Map<String, Object> request) {

        String logMessage = "POST /api/marketplace/buyPersona";

        String username = request.get("username").toString();
        String personaName = request.get("personaName").toString();

        log.info(logMessage + " username: " + username + " personaName: " + personaName);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'username' is required.");
        }
        else if (personaName == null || personaName.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'personaName' is required.");
        }

        marketplaceService.buyPersona(username, personaName);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaName);
    }

    @PostMapping("/seller-personas")
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

    @DeleteMapping("/remove")
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

//    private Map<String, String> extractUsernameAndPersonaname(Map<String, Object> request) {
//        String username = request.get("username").toString();
//        String personaName = request.get("personaName").toString();
//
//        if (username == null || username.isEmpty()) {
//            throw new IllegalArgumentException("Error: 'username' is required.");        }
//        if (personaName == null || personaName.isEmpty()) {
//            throw new IllegalArgumentException("Error: 'personaName' is required.");
//        }
//
//        // Return a map containing the validated values
//        Map<String, String> result = new HashMap<>();
//        result.put("username", username);
//        result.put("personaName", personaName);
//        return result;
//    }

}
