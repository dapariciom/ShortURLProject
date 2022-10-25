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

    @JsonProperty("originalurl")
    private String originalUrl;

    private String alias;

    @JsonProperty("shortUrl")
    private String shortUrl;

    @JsonProperty("completeshorturl")
    private String completeShorUrl;

    @JsonProperty("expirationdate")
    private LocalDateTime expirationDate;

}
