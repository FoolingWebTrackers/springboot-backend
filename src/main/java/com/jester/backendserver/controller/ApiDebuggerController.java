package com.jester.backendserver.controller;

import com.jester.backendserver.service.ImageService;
import com.jester.backendserver.service.UrlGeneratorService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/debug")
public class ApiDebuggerController {

    @Autowired
    private UrlGeneratorService apiService;
    @Autowired
    private ImageService imageService;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiDebuggerController.class);



    @GetMapping("/get-links")
    public List<String> getLinks(@RequestParam String personaDesc) {
        log.info("GET /api/debug/get-links");
        return apiService.getLinksFromApi(personaDesc);
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateImage(@RequestParam String personaDesc) {
        log.info("POST /api/debug/generate");
        String b64_image = imageService.generateImage(personaDesc);
        assert imageService.isBase64(b64_image);
        // Turn to binary for display
        byte[] img_bytes = Base64.getDecoder().decode(b64_image);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(img_bytes);
    }
}