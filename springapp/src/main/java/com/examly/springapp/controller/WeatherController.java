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
import com.examly.springapp.model.WeatherResponse;

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
            return ResponseEntity.badRequest().body(new WeatherResponse());
        }

        try {
            String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            
            if (response == null || response.getMain() == null) {
                WeatherResponse errorResponse = new WeatherResponse();
                WeatherResponse.Main main = new WeatherResponse.Main();
                main.setTemp(0.0);
                errorResponse.setMain(main);
                return ResponseEntity.ok(errorResponse);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            WeatherResponse errorResponse = new WeatherResponse();
            WeatherResponse.Main main = new WeatherResponse.Main();
            main.setTemp(0.0);
            errorResponse.setMain(main);
            return ResponseEntity.ok(errorResponse);
        }
    }
} 