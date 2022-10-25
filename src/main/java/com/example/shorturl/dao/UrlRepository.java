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

    /**
     * This method is used to check if an UrlEntity exists
     * with a specific alias
     *
     * @param alias This is the alias to look for
     * @return Boolean Returns true if an entity was found
     */
    Boolean existsByAlias(String alias);

}
