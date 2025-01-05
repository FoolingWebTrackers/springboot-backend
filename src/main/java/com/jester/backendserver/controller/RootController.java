package com.jester.backendserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RootController {
    @Autowired
    private WebEndpointDiscoverer webEndpointDiscoverer;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

//    @GetMapping
//    public String status() {
//        return "Welcome to the Jester API!";
//    }

    @GetMapping()
    public Map<String, List<String>> getAllEndpoints() {
        return requestMappingHandlerMapping.getHandlerMethods()
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue().getBeanType().getName(),
                        Collectors.mapping(entry -> entry.getKey().toString(), Collectors.toList())
                ));
    }
}