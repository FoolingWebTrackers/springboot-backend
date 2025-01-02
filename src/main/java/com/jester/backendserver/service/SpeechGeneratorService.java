package com.jester.backendserver.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.core.io.Resource;
import org.springframework.http.*;

import java.nio.file.*;


@Service
public class SpeechGeneratorService {

    String SPEECH_API_URL = "https://api.perplexity.ai/chat/completions";
    String SPEECH_API_KEY = "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";
    String AUDIO_API_URL = "https://api.playht.com/v1/stream";
    String AUDIO_API_KEY = "";
    String AUDIO_USER_ID = "";

    public String getSpeechFromApi(String personaDesc) {

        String contentBody = String.format("""
                {
                    "messages": [
                        {
                            "content": "Act as a "%s" and generate a casual conversational speech as you are speaking.",
                            "role": "user"
                        }
                    ],
                    "model": "llama-3.1-sonar-small-128k-online"
                }
                """, personaDesc);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SPEECH_API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(contentBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(SPEECH_API_URL, HttpMethod.POST, request, String.class);
            String responseBody = response.getBody();

            // Extract links from the response
            return responseBody;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch links", e);
        }
    }

    public String streamAudio(String text) {
    
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + AUDIO_API_KEY);
        headers.set("X-User-Id", AUDIO_USER_ID);

        // Request body
        String requestBody = String.format("{\"text\":\"%s\", \"voiceEngine\":\"PlayDialog\"}", text);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Stream audio and save to file
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Resource> response = restTemplate.exchange(AUDIO_API_URL, HttpMethod.POST, entity, Resource.class);
            if (response.getBody() != null) {
                InputStream inputStream = response.getBody().getInputStream();
                Path outputPath = Paths.get("output.mp3");

                // Write the audio stream to a file
                Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);

                return "Audio saved as output.mp3";
            } else {
                return "Failed to get audio stream";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public byte[] streamAudioByte(String text) {
    
    // Set headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + AUDIO_API_KEY);
    headers.set("X-User-Id", AUDIO_USER_ID);

    // Request body
    String requestBody = String.format("{\"text\":\"%s\", \"voiceEngine\":\"PlayDialog\"}", text);
    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

    try {
        // Stream audio and return as a byte array
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Resource> response = restTemplate.exchange(AUDIO_API_URL, HttpMethod.POST, entity, Resource.class);

        if (response.getBody() != null) {
            try (InputStream inputStream = response.getBody().getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                // Write audio data to the ByteArrayOutputStream
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                return byteArrayOutputStream.toByteArray();
            }
        } else {
            throw new RuntimeException("Failed to get audio stream");
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error: " + e.getMessage());
    }
}


}
