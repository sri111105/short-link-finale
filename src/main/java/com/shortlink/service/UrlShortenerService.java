package com.shortlink.service;

import com.shortlink.entity.UrlMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UrlShortenerService {

    private final UrlMappingRepository repository;

    @Value("${app.base-url}")
    private String baseUrl;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    public UrlShortenerService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public UrlMapping shortenUrl(String longUrl) {
        // Check if unique constraint might be an issue, but for now simple random generation
        String shortCode = generateShortCode();
        while (repository.findByShortCode(shortCode).isPresent()) {
            shortCode = generateShortCode();
        }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortCode(shortCode);
        
        log.info("Successfully shortened URL: {} -> {}", longUrl, shortCode);
        
        return repository.save(urlMapping);
    }

    public Optional<UrlMapping> getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode);
    }

    public void incrementClickCount(UrlMapping urlMapping) {
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        repository.save(urlMapping);
    }
    
    public List<UrlMapping> getAllUrls() {
        return repository.findAll();
    }

    private String generateShortCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public boolean deleteUrl(Long id) {
        Optional<UrlMapping> urlMapping = repository.findById(id);
        if (urlMapping.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
