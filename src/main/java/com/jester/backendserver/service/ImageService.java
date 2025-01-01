package com.jester.backendserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class ImageService {
    String API_KEY = "key-37AeoLIoojTqLdbo1d4JDQY9UGvaokAAJ1q2f46ijW1Us5E5ARUZN5kjJyLKglWeoW8QF7W644Jwz0J2egC5fwnUJLw7WX5Z";
    String API_URL = "https://api.getimg.ai/v1/flux-schnell/text-to-image";

    public String generateImage(String personaDesc) {
        String requestBody = String.format("""
                {
                    "prompt": "%s",
                    "width": 256,
                    "height": 256,
                    "response_format": "b64"
                }
                """, personaDesc);

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("content-type", "application/json");
        headers.set("authorization", "Bearer " + API_KEY);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.get("image").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error generating image", e);
        }
    }
    public boolean isBase64(String base64) {
        try {
            Base64.getDecoder().decode(base64);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}