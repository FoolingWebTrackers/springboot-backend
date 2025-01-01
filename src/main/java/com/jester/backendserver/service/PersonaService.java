package com.jester.backendserver.service;

import com.jester.backendserver.model.Persona;
import com.jester.backendserver.model.User;
import com.jester.backendserver.repository.PersonaProcedureRepository;
import com.jester.backendserver.repository.PersonaRepository;
import com.jester.backendserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonaService {
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PersonaProcedureRepository personaProcedureRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UrlGeneratorService urlGeneratorService;
    @Autowired
    private UserRepository userRepository;

    public List<Persona> getPersonas() {
        return personaRepository.findAll();
    }

    public Persona getPersona(Integer pid) {
        return personaRepository.findById(Long.valueOf(pid))
                .orElseThrow(() -> new NoSuchElementException("Persona not found"));
    }

    public List<Persona> getPersonasByUsername(String userName) {
        Optional<User> opt_user = userRepository.findByUsername(userName);
        if (opt_user.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }
        User user = opt_user.get();
        return user.getPersonas();
    }

    public Persona registerPersona(String userName, String personaName, String description, Boolean generatePhoto) {
        Integer pid = personaProcedureRepository.createPersona(userName, personaName, description);
        if (generatePhoto) {
            String image_b64 = imageService.generateImage(description);
            setImageBase64(pid, image_b64);
        }
        personaProcedureRepository.insertPersonaLinks(pid, urlGeneratorService.getLinksFromApi(description));
        return getPersona(pid);
    }

    public Persona addURLs(List<String> urls, Integer pid) {
        personaProcedureRepository.insertPersonaLinks(pid, urls);
        return getPersona(pid);
    }

    public byte[] getImageBytes(Integer pid) {
        Persona persona = getPersona(pid);
        return persona.getImage();
    }

    public String getImageBase64(Integer pid) {
        Persona persona = getPersona(pid);
        return persona.getImageBase64();
    }

    public void setImageBase64(Integer pid, String imageBase64) {
        Persona persona = getPersona(pid);
        persona.setImageBase64(imageBase64);
        personaRepository.save(persona);
    }
}
