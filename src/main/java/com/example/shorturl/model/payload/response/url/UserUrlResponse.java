package com.example.shorturl.model.payload.response.url;

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
public class UserUrlResponse {
    private String originalUrl;
    private String alias;
    private String shorUrl;
    private String completeShorUrl;
    private LocalDateTime expirationDate;
}
