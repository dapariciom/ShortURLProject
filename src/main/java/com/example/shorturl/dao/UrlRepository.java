package com.example.shorturl.dao;

import com.example.shorturl.model.url.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends MongoRepository<UrlEntity, Long> {

    /**
     * This method is used to look for an UrlEntity with a specific
     * shortUrl from the database
     *
     * @param shortUrl This is the shortUrl to look for
     * @return Optional<UrlEntity> This returns the found url entity
     */
    Optional<UrlEntity> findByShortUrl(String shortUrl);

}
