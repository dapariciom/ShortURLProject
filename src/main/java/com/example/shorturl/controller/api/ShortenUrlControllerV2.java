package com.example.shorturl.controller.api;

import com.example.shorturl.model.payload.request.url.UserUrlRequest;
import com.example.shorturl.model.payload.response.url.UserUrlResponse;
import com.example.shorturl.model.url.UrlEntity;
import com.example.shorturl.service.url.ShortenUrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/url/user")
public class ShortenUrlControllerV2 {

    private final ShortenUrlService shortenUrlService;

    public ShortenUrlControllerV2(final ShortenUrlService shortenUrlService){
        this.shortenUrlService = shortenUrlService;
    }

    @PostMapping
    public ResponseEntity<UserUrlResponse> shortUrl(@RequestBody UserUrlRequest userUrlRequest) {

        if(Objects.isNull(userUrlRequest) || StringUtils.isEmpty(userUrlRequest.getUrl()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url request is missing or empty");

        Optional<UrlEntity> optionalUrl = shortenUrlService.userShortUrl(userUrlRequest);

        UrlEntity url = optionalUrl.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url is missing"));

        return new ResponseEntity<>(
                UserUrlResponse.builder()
                        .shorUrl(url.getShortUrl())
                        .alias(url.getAlias())
                        .completeShorUrl(url.getCompleteShortUrl())
                        .originalUrl(url.getOriginalUrl())
                        .expirationDate(url.getExpirationDate())
                        .build(), HttpStatus.OK);

    }

}