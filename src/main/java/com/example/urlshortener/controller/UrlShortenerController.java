package com.example.urlshortener.controller;

import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.service.UrlShortenerService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService service;

    @PostMapping("/api/urls/shorten")
    public ResponseEntity<UrlMapping> shortenUrl(@RequestBody Map<String, String> request) {
        String longUrl = request.get("longUrl");
        if (longUrl == null || longUrl.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        UrlMapping created = service.shortenUrl(longUrl);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/api/urls/stats")
    public List<UrlMapping> getStats() {
        return service.getAllUrls();
    }

    @GetMapping("/{shortCode:[a-zA-Z0-9]{6}}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        Optional<UrlMapping> mapping = service.getOriginalUrl(shortCode);
        if (mapping.isPresent()) {
            service.incrementClickCount(mapping.get());
            response.sendRedirect(mapping.get().getLongUrl());
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "URL not found");
        }
    }

    @DeleteMapping("/api/urls/{id}")
    public ResponseEntity<Map<String, String>> deleteUrl(@PathVariable Long id) {
        boolean deleted = service.deleteUrl(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "URL deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "URL not found"));
        }
    }
}
