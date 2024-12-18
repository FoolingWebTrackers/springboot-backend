package com.jester.backendserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping
    public String status() {
        return "Welcome to the Jester API!";
    }
    // TODO: Return a string that has information about the server api and endpoints.
}