package com.example.shorturl.integration.test;

import com.example.shorturl.dao.UrlRepository;
import com.example.shorturl.model.url.UrlEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
public class MongoDbTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private UrlRepository urlRepository;

    @AfterEach
    void cleanUp() {
        this.urlRepository.deleteAll();
    }

    @Test
    void saveAndReturnUrlsFromRepository() {
        this.urlRepository.save(UrlEntity.builder().id(1).originalUrl("https://www.youtube.com/").build());
        this.urlRepository.save(UrlEntity.builder().id(2).originalUrl("https://www.facebook.com/").build());
        this.urlRepository.save(UrlEntity.builder().id(3).originalUrl("https://www.instagram.com/").build());
        List<UrlEntity> urlList = urlRepository.findAll();
        assertEquals(3, urlList.size());
    }

}