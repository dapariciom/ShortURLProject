package com.example.shorturl.model.payload.request.url;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUrlRequest {

    private String url;

    private String alias;

    @JsonProperty("expirationtime")
    private Long expirationTime;

}
