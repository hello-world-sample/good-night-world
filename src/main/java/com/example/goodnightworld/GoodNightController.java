package com.example.goodnightworld;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodNightController {

    @Value("${greeting.name}")
    private String name;

    @GetMapping("/api/good-night")
    public String goodNight() {
        return "Good night " + name;
    }
}
