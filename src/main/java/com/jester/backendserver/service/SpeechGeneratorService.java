package com.jester.backendserver.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.core.io.Resource;
import org.springframework.http.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
                            "content": "Act as a '%s' and generate a casual conversational speech, having 100 words. Only output the speech itself. Do not output anything else.",
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

    public byte[] getSpeech(String text) throws IOException {
        String apiKey = "ca647d17c7c64b5caacda8862e6cd77e";
        String VOICERSS_API_URL = "http://api.voicerss.org/";

        // URL-encode the text to handle special characters and spaces
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        // Build the URL
        String urlString = VOICERSS_API_URL + "?" +
                "key=" + apiKey +
                "&hl=en-us" +
                "&src=" + encodedText +
                "&c=MP3";

        // Create a connection to the URL
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Check for HTTP response code
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("VoiceRSS API returned an error: " + responseCode);
        }

        // Read the audio data from the API response
        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }
}


