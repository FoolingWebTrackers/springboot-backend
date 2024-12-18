package com.jester.backendserver.service;

import com.jester.backendserver.model.Persona;
import com.jester.backendserver.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {
    @Autowired
    private PersonaRepository personaRepository;

    public List<Persona> getPersonas() {
        return personaRepository.findAll();
    }
}
