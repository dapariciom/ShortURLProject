package com.example.shorturl.dao;

import com.example.shorturl.model.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends MongoRepository<UrlEntity, Integer> {
    List<UrlEntity> findByShortUrl(String url);
}
