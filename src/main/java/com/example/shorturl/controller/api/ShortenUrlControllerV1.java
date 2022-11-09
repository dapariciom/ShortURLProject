package com.example.shorturl.controller.api;


import com.example.shorturl.model.payload.request.url.UrlRequest;
import com.example.shorturl.model.payload.response.url.UrlResponse;
import com.example.shorturl.model.url.UrlEntity;
import com.example.shorturl.service.url.ShortenUrlService;
import com.example.shorturl.utils.exceptions.UrlNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/url")
public class ShortenUrlControllerV1 {

    private final ShortenUrlService shortenUrlService;

    public ShortenUrlControllerV1(final ShortenUrlService shortenUrlService){
        this.shortenUrlService = shortenUrlService;
    }

    @PostMapping
    public ResponseEntity<UrlResponse> shortUrl(@RequestBody UrlRequest urlRequest) throws UrlNotFoundException {

        if(Objects.isNull(urlRequest) || StringUtils.isEmpty(urlRequest.getUrl()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url request is missing or empty");

        UrlEntity url = shortenUrlService.shortUrl(urlRequest);

        return new ResponseEntity<>(
                UrlResponse.builder()
                        .id(url.getId())
                        .shorUrl(url.getShortUrl())
                        .completeShorUrl(url.getCompleteShortUrl())
                        .originalUrl(url.getOriginalUrl())
                        .expirationDate(url.getExpirationDate())
                        .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlEntity> findById(@PathVariable Long id) throws UrlNotFoundException {

        Optional<UrlEntity> optionalUrl = shortenUrlService.findById(id);

        UrlEntity url = optionalUrl.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url not found"));

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(url);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){

        shortenUrlService.softDeleteById(id);

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(headers).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll(){

        shortenUrlService.softDeleteAll();

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(headers).build();
    }

    @GetMapping
    public ResponseEntity<List<UrlEntity>> getUrls(){

        List<UrlEntity> urlList = shortenUrlService.findAll();

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(urlList);
    }

}
