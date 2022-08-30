package com.example.shorturl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrlResponse {
    private String originalUrl;
    private String shorUrl;
    private String completeShorUrl;
    private LocalDateTime expirationDate;
}
