package com.example.demo.dao;

import com.example.demo.model.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UrlRepository extends MongoRepository<UrlEntity, Integer> {
    List<UrlEntity> findByShortUrl(String url);
}
