package com.jester.backendserver.service;

import com.jester.backendserver.controller.MarketPlaceController;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UrlGeneratorService.class);




    public List<String> getLinksFromApi(String personaDesc) {

        String contentBody = String.format(
"""
{
    "messages": [
        {
            "content": "%s",
            "role": "user"
        }
    ],
    "model": "llama-3.1-sonar-small-128k-online"
}
""", getPrompt(personaDesc).replace("\n", "\\n"));

        log.warn(contentBody);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(contentBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
            String responseBody = response.getBody();
            System.out.println(responseBody);
            // Extract links from the response
            List<String> links = extractLinks(responseBody);
            System.out.println(links);
            return links;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch links", e);
        }
    }

    public static List<String> extractLinks(String content) {
        Set<String> links = new HashSet<>();

        // Regular expression to match URLs in the content
        String regex = "https?://[\\w/\\-?=%.]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        // Iterate through matches and add them to the list
        while (matcher.find()) {
            links.add(matcher.group());
        }

        log.info("Extracted {} links", links.size());

        return new ArrayList<>(links);
    }

    private String getPrompt(String personaDesc){
        return String.format(
                """
                        You are an intelligent system designed to find the most relevant URLs based on a given persona description. \s
                        Your task is to provide a list of links tailored to the persona's interests. \s
                        The links can include websites, search queries, X entries, web articles, blogs, social media posts, pdf files, products in e-commerce marketplaces etc.. Find anything related.\s
                        You have to find varied links which illustrate what the given persona can search on the web.
                        
                        **Persona Description:**
                        %s
                        
                        Output in the following format: ['https://www.link1.com', 'https://www.link2', ... ]
                        Do NOT include anything else than the link list.
                        Do NOT include any descriptions, output ONLY the raw link array.
                        
                        Generate 200 links now. DO NOT GIVE ANY DUPLICATE LINKS!!
                """
        , personaDesc);
    }
}