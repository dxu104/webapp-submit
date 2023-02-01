package com.csye6225HW1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeverIsHealthyController {
    @GetMapping("/healthz")
    public String heahlth(){
        return "Sever is Healthy";
    }
}
