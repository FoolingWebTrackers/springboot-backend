package com.jester.backendserver.controller;

import com.jester.backendserver.model.Marketplace;
import com.jester.backendserver.service.SpeechGeneratorService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    @Autowired
    SpeechGeneratorService speechGeneratorService;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MarketPlaceController.class);

    @PostMapping
    public ResponseEntity<?> getSpeechAudio(@Valid @RequestBody Map<String, Object> request) throws IOException {
        String logMessage = "POST /api/audio";

        String description = request.get("speech").toString();
        log.info(logMessage + " persona description: " + description);

        //List<Persona> personas = .getPersonasByUsername(username);
        byte[] speech = speechGeneratorService.getSpeech(description);
        System.out.println(speech.toString());

        /*
        try (FileOutputStream fileOutputStream = new FileOutputStream("output.mp3")) {
            fileOutputStream.write(speech);
            System.out.println("Audio saved as output.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        String base64Audio = Base64.getEncoder().encodeToString(speech);
        log.info("Converted audio to Base64.");

        // Return the Base64 string as the response
        return ResponseEntity.ok(base64Audio);
        //return ResponseEntity.ok(speech);
    }

    @PostMapping("/generatespeech")
    public ResponseEntity<?> generateSpeech(@Valid @RequestBody Map<String, Object> request) {

        String logMessage = "POST /api/audio";

        String description = request.get("personaDesc").toString();
        //String personaName = request.get("personaName").toString();
        log.info(logMessage + " persona description: " + description);

        //List<Persona> personas = .getPersonasByUsername(username);
        String speech = speechGeneratorService.getSpeechFromApi(description);
        System.out.println(speech);
        return ResponseEntity.ok(speech);
    }


}
