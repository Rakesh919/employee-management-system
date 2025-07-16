package com.company.controllers.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class sampleController {

    @GetMapping("/")
    public String sample() {
        return "<html><body><h1>Hello from Spring Boot</h1></body></html>";
    }
}