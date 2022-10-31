package com.example.shorturl.model.payload.response.url;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("original_url")
    private String originalUrl;

    private String alias;

    @JsonProperty("short_url")
    private String shortUrl;

    @JsonProperty("complete_short_url")
    private String completeShorUrl;

    private long createdBy;

    @JsonProperty("expiration_date")
    private LocalDateTime expirationDate;

}
