package com.example.shorturl.service;

import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.model.UrlRequest;

import java.util.Optional;

public interface IShortenUrlService {

    Optional<UrlEntity> shortUrl(UrlRequest urlRequest);
}
