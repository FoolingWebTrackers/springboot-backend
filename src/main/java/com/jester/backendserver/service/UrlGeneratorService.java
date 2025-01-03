package com.jester.backendserver.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlGeneratorService {
    // Perplexity
    String API_URL = "https://api.perplexity.ai/chat/completions";
    String API_KEY = "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";

    public List<String> getLinksFromApi(String personaDesc) {

        String contentBody = String.format("""
                {
                    "messages": [
                        {
                            "content": "Search the web to find 200 website links including videos, social media posts, X entries, web articles, pdf files and products in e-commerce marketplaces that %s search. Please return exactly one hundred web links, they must start with https, as a list. Do not give any extra recommendations than the links.",
                            "role": "user"
                        }
                    ],
                    "model": "llama-3.1-sonar-small-128k-online"
                }
                """, personaDesc);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(contentBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
            String responseBody = response.getBody();

            // Extract links from the response
            return extractLinks(responseBody);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch links", e);
        }
    }

    private List<String> extractLinks(String content) {
        Set<String> linkSet = new HashSet<>();
        Pattern pattern = Pattern.compile("https?://[^\\s]+");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String url = matcher.group();
            linkSet.add(url.replaceAll("[.,!?)]*$", "").trim());
        }

        return List.copyOf(linkSet);
    }
}