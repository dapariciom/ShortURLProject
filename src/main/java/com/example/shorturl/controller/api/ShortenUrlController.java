package com.example.shorturl.controller.api;

import com.example.shorturl.service.ShortenUrlService;
import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.model.UrlRequest;
import com.example.shorturl.model.UrlResponse;
import com.example.shorturl.utils.UrlNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/url")
public class ShortenUrlController {

    private final ShortenUrlService shortenUrlService;

    @Autowired
    public ShortenUrlController(final ShortenUrlService shortenUrlService){
        this.shortenUrlService = shortenUrlService;
    }

    @PostMapping
    public ResponseEntity<UrlResponse> shortUrl(@RequestBody UrlRequest urlRequest) throws UrlNotFoundException {

        if(Objects.isNull(urlRequest) || StringUtils.isEmpty(urlRequest.getUrl())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url request is missing or empty");
        }

        Optional<UrlEntity> optionalUrl = shortenUrlService.shortUrl(urlRequest);

        if(optionalUrl.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url is missing");
        }

        return new ResponseEntity<>(
                UrlResponse.builder()
                        .shorUrl(optionalUrl.get().getShortUrl())
                        .completeShorUrl(optionalUrl.get().getCompleteShortUrl())
                        .originalUrl(optionalUrl.get().getOriginalUrl())
                        .expirationDate(optionalUrl.get().getExpirationDate())
                        .build(), HttpStatus.OK);
    }

    @GetMapping("/redirect/{shortLink}")
    public ResponseEntity<UrlResponse> redirectUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        //TODO: Handle empty path variable
        if(StringUtils.isEmpty(shortLink)){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        }

        UrlEntity url = shortenUrlService.getEncodedUrl(shortLink);

        //TODO: Handle url is not in database
        if(url == null){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        }

        //TODO: Handle url is has expired
//        if(url.getExpirationDate().isBefore(LocalDateTime.now())){
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
//        }

        response.sendRedirect(url.getOriginalUrl());

        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UrlEntity>> findById(@PathVariable Integer id){
        Optional<UrlEntity> urlList = shortenUrlService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(urlList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){
        shortenUrlService.softDeleteById(id);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll(){
        shortenUrlService.softDeleteAll();
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    @GetMapping
    public ResponseEntity<List<UrlEntity>> getUrls(){
        List<UrlEntity> urlList = shortenUrlService.findAll();
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(urlList);
    }

}
