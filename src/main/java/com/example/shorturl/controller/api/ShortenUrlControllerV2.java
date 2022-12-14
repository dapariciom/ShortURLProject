package com.example.shorturl.controller.api;

import com.example.shorturl.model.payload.request.url.UserUrlRequest;
import com.example.shorturl.model.payload.response.url.UserUrlResponse;
import com.example.shorturl.model.url.UrlEntity;
import com.example.shorturl.security.MyUserDetails;
import com.example.shorturl.service.url.ShortenUrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/v2/url")
public class ShortenUrlControllerV2 {

    private final ShortenUrlService shortenUrlService;

    public ShortenUrlControllerV2(final ShortenUrlService shortenUrlService){
        this.shortenUrlService = shortenUrlService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserUrlResponse> shortUrl(@RequestBody UserUrlRequest userUrlRequest) {

        if(Objects.isNull(userUrlRequest) || StringUtils.isEmpty(userUrlRequest.getUrl()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url request is missing or empty");

        Long userId = getAuthenticatedUserId();

        Optional<UrlEntity> optionalUrl = shortenUrlService.findByAliasAndCreatedBy(userUrlRequest.getAlias(), userId);

        UrlEntity url = optionalUrl.isPresent() ? optionalUrl.get() :
                shortenUrlService.userShortUrl(userId, userUrlRequest);

        return new ResponseEntity<>(
                UserUrlResponse.builder()
                        .shortUrl(url.getShortUrl())
                        .createdBy(url.getCreatedBy())
                        .alias(url.getAlias())
                        .completeShorUrl(url.getCompleteShortUrl())
                        .originalUrl(url.getOriginalUrl())
                        .expirationDate(url.getExpirationDate())
                        .build(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UrlEntity>> getUrls(){

        Long userId = getAuthenticatedUserId();

        List<UrlEntity> urlList = shortenUrlService.findByCreatedBy(userId);

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(urlList);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){

        Long userId = getAuthenticatedUserId();

        Optional<UrlEntity> optionalUrl = shortenUrlService.findByIdAndCreatedBy(id, userId);

        UrlEntity url = optionalUrl.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url not found"));

        shortenUrlService.softDeleteById(url.getId());

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(headers).build();
    }

    public Long getAuthenticatedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userEntity = (MyUserDetails) authentication.getPrincipal();
        return userEntity.getId();
    }

}