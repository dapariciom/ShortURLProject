package com.example.shorturl.model;

import lombok.*;

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
