package com.example.shorturl.controller.api;

import com.example.shorturl.model.url.UrlEntity;
import com.example.shorturl.model.payload.request.url.UrlRequest;
import com.example.shorturl.model.payload.response.url.UrlResponse;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        Optional<UrlEntity> optionalUrl = shortenUrlService.shortUrl(urlRequest);

        UrlEntity url = optionalUrl.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url is missing"));

        return new ResponseEntity<>(
                UrlResponse.builder()
                        .shorUrl(optionalUrl.get().getShortUrl())
                        .completeShorUrl(optionalUrl.get().getCompleteShortUrl())
                        .originalUrl(optionalUrl.get().getOriginalUrl())
                        .expirationDate(optionalUrl.get().getExpirationDate())
                        .build(), HttpStatus.OK);
    }

    @GetMapping("/redirect/{shortUrl}")
    public ResponseEntity<UrlResponse> redirectUrl(@PathVariable String shortUrl, HttpServletResponse response) throws UrlNotFoundException, IOException {

        HttpHeaders headers = new HttpHeaders();

        if(StringUtils.isEmpty(shortUrl))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url request is missing or empty");

        Optional<UrlEntity> optionalUrl = shortenUrlService.getEncodedUrl(shortUrl);

        UrlEntity url = optionalUrl.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url not found"));

        if(url.getIsDeleted())
            throw new ResponseStatusException(HttpStatus.GONE, "Url has been deleted");

        if(url.getIsExpired())
            throw new ResponseStatusException(HttpStatus.GONE, "Url has expired");

        //TODO: Handle IOException
        response.sendRedirect(url.getOriginalUrl());

        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
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
