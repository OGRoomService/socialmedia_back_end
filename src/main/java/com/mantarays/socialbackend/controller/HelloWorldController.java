package com.mantarays.socialbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorldController {
    
    @GetMapping("/sayhello")
    public String sayHelloWorld() {
        return "Hello World!";
    }
}
