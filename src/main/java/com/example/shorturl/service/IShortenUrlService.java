package com.example.shorturl.service;

import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.model.UrlRequest;

import java.util.Optional;

public interface IShortenUrlService {

    /**
     * This method is used to convert a long URL to a shorter encoded URL.
     * It saves an entity to the database that links both URL's
     *
     * @param urlRequest This request object contains the long URL provided by the user
     * @return Optional<UrlEntity> This returns the entity created that contains the
     * information of the long and short URL
     */
    Optional<UrlEntity> shortUrl(UrlRequest urlRequest);
}
