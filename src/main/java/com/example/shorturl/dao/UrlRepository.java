package com.example.shorturl.dao;

import com.example.shorturl.model.url.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
     * This method is used to check if an UrlEntity exists with a specific alias
     * created by a specific user. It includes parameters to look for an entity
     * that has been or not been deleted or expired
     *
     * @param alias This is the alias to look for
     * @param createdBy This is the userId that created the entity to look for
     * @param isDeleted True if the entity has been deleted
     * @param isExpired True if the entity has been expired
     * @return Optional<UrlEntity> This returns the found url entity
     */
    Optional<UrlEntity> findByAliasAndCreatedByAndIsDeletedAndIsExpired(String alias, Long createdBy, Boolean isDeleted, Boolean isExpired);

    /**
     * This method is used to look for a list of UrlEntities created by
     * a specific user. It includes parameters to look for entities that have
     * been or not been deleted or expired
     *
     * @param createdBy This is the userId that created the entity to look for
     * @param isDeleted True if the entity has been deleted
     * @param isExpired True if the entity has been expired
     * @return List<UrlEntity> The list of found UrlEntities
     */
    List<UrlEntity> findByCreatedByAndIsDeletedAndIsExpired(Long createdBy, Boolean isDeleted, Boolean isExpired);

    /**
     * This method is used to look for an UrlEntity with a specific
     * id created by a specific user
     *
     * @param id This is the id to look for
     * @param createdBy This is the userId that created the entity to look for
     * @return Optional<UrlEntity> This returns the found url entity
     */
    Optional<UrlEntity> findByIdAndCreatedBy(Long id, Long createdBy);

}
