package com.example.urlshortener.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048) // URLs can be long
    private String longUrl;

    @Column(nullable = false, unique = true)
    private String shortCode;

    private long clickCount;

    private LocalDateTime createdDate;

    public UrlMapping() {}

    public UrlMapping(Long id, String longUrl, String shortCode, long clickCount, LocalDateTime createdDate) {
        this.id = id;
        this.longUrl = longUrl;
        this.shortCode = shortCode;
        this.clickCount = clickCount;
        this.createdDate = createdDate;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.clickCount = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLongUrl() { return longUrl; }
    public void setLongUrl(String longUrl) { this.longUrl = longUrl; }
    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public long getClickCount() { return clickCount; }
    public void setClickCount(long clickCount) { this.clickCount = clickCount; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
