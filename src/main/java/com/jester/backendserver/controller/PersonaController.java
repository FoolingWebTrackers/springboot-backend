package com.jester.backendserver.controller;

import com.jester.backendserver.model.Persona;
import com.jester.backendserver.model.PersonaRegistrationDTO;
import com.jester.backendserver.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    @Autowired
    private PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> getAllPersonas() {
        List<Persona> personas = personaService.getPersonas();
        return ResponseEntity.ok(personas);
    }

    @PostMapping("/register")
    public ResponseEntity<Persona> registerPersona(@RequestBody PersonaRegistrationDTO persona) {
        Persona newPersona =  personaService.registerPersona(persona.getUserName(), persona.getPersonaName(), persona.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(newPersona);
    }
}
