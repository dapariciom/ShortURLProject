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
public class UrlResponse {

    @JsonProperty("originalurl")
    private String originalUrl;

    @JsonProperty("shorturl")
    private String shorUrl;

    @JsonProperty("completeshorturl")
    private String completeShorUrl;

    @JsonProperty("expirationdate")
    private LocalDateTime expirationDate;

}
