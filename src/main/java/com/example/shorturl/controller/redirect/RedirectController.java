package com.example.shorturl.controller.redirect;

import com.example.shorturl.model.payload.response.url.UrlResponse;
import com.example.shorturl.model.url.UrlEntity;
import com.example.shorturl.service.url.ShortenUrlService;
import com.example.shorturl.utils.exceptions.UrlNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/redirect")
public class RedirectController {

    private final ShortenUrlService shortenUrlService;

    public RedirectController(final ShortenUrlService shortenUrlService){
        this.shortenUrlService = shortenUrlService;
    }

    @GetMapping("/{shortUrl}")
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

}
