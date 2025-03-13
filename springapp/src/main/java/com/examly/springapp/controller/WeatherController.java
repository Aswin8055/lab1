package com.examly.springapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam(required = false) String city) {
        if (city == null || city.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("City parameter is required");
        }

        try {
            String url = String.format("%s?q=%s&appid=%s", apiUrl, city, apiKey);
            Object response = restTemplate.getForObject(url, Object.class);
            
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found");
        }
    }
} 