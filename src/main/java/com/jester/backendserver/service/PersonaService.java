package com.jester.backendserver.service;

import com.jester.backendserver.model.Persona;
import com.jester.backendserver.repository.PersonaProcedureRepository;
import com.jester.backendserver.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PersonaProcedureRepository personaProcedureRepository;

    public List<Persona> getPersonas() {
        return personaRepository.findAll();
    }

    public Persona getPersona(Integer pid) {
        return personaRepository.getPersonaById(pid);
    }

    public Persona registerPersona(String userName, String personaName, String description) {
        Integer pid = personaProcedureRepository.createPersona(userName, personaName, description);
        return getPersona(pid);
    }

    public Persona addURLs(List<String> urls, Integer pid) {
        personaProcedureRepository.insertPersonaLinks(pid, urls);
        return getPersona(pid);
    }
}
