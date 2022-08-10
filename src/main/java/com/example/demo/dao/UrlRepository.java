package com.example.demo.dao;

import com.example.demo.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, Integer> {
}
