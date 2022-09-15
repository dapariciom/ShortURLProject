package com.example.shorturl.service.url;

import com.example.shorturl.model.url.UrlEntity;
import com.example.shorturl.model.url.UrlRequest;

import java.util.List;
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

    /**
     * This method uses an encoded short URL to obtain a linked long URL
     *
     * @param url This is the short URL in String format
     * @return Optional<UrlEntity> This returns the entity that contains the long URL and
     * other related parameters
     */
    Optional<UrlEntity> getEncodedUrl(String url);

    /**
     * This method is used to look for an UrlEntity with a specific id
     *
     * @param id This is the unique identifier to look for
     * @return Optional<UrlEntity> This returns the found entity
     */
    Optional<UrlEntity> findById(Long id);

    /**
     * This method is used to find all the UrlEntity's that have not been soft deleted. This means
     * return all entities with attribute isDeleted equal to false
     *
     * @return List<UrlEntity> This returns a list with the found entities
     */
    List<UrlEntity> findAll();

    /**
     * This method makes a soft delete to the UrlEntity with the specified id. This means
     * sets the isDeleted attribute to true
     *
     * @param id This is the unique identifier to delete
     */
    void softDeleteById(Long id);

    /**
     * This method makes a soft delete to all the found UrlEntity's. This means
     * sets the isDeleted attribute to true to all the entities
     */
    void softDeleteAll();


}
