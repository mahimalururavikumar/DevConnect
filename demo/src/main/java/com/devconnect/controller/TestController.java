package com.devconnect.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String securedEndpoint() {
        return "Access granted. JWT is working.";
    }
}
