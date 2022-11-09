package com.example.shorturl.integration.test;

import com.example.shorturl.dao.UrlRepository;
import com.example.shorturl.model.payload.request.url.UrlRequest;
import com.example.shorturl.model.payload.response.url.UrlResponse;
import com.example.shorturl.model.url.UrlEntity;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlIntegrationTestV1 {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private UrlRepository urlRepository;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/v1/url");
    }

    @AfterEach
    void cleanUp() {
        this.urlRepository.deleteAll();
    }

    @Test
    void postAnUrlRequestAndReceivedAnEncodedUrlResponse(){

        Integer HASH_LENGTH = 8;

        String urlTest = "https://www.youtube.com/";

        UrlRequest urlRequest = UrlRequest.builder().url(urlTest).build();

        UrlResponse urlResponse = restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);

        assertEquals(urlTest, urlResponse.getOriginalUrl());
        assertThat(urlResponse.getCompleteShorUrl(), CoreMatchers.containsString(urlResponse.getShorUrl()));
        assertEquals(urlResponse.getShorUrl().length(), HASH_LENGTH);

        assertEquals(1, urlRepository.findAll().size());
    }

    @Test
    void postAnUrlRequestAndFindById(){

        String urlTest = "https://www.youtube.com/";

        UrlRequest urlRequest = UrlRequest.builder().url(urlTest).build();

        UrlResponse urlResponse = restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);

        Long urlId = urlResponse.getId();

        UrlEntity urlEntity = restTemplate.getForObject(baseUrl + "/{id}", UrlEntity.class, urlId);

        assertEquals(urlEntity.getId(), urlId);
        assertEquals(urlEntity.getOriginalUrl(), urlTest);
    }

    @Test
    void postAnUrlRequestAndDeleteById(){

        String urlTest = "https://www.youtube.com/";

        UrlRequest urlRequest = UrlRequest.builder().url(urlTest).build();

        UrlResponse urlResponse = restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);

        assertEquals(1, urlRepository.findAll().size());

        Long urlId = urlResponse.getId();

        restTemplate.delete(baseUrl + "/{id}", urlId);

        UrlEntity urlEntity = restTemplate.getForObject(baseUrl + "/{id}", UrlEntity.class, urlId);

        assertTrue(urlEntity.getIsDeleted());
    }

    @Test
    void postUrlsAndDeleteAll(){

        String urlTest = "https://www.youtube.com/";

        UrlRequest urlRequest = UrlRequest.builder().url(urlTest).build();

        restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);
        restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);
        restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);

        assertEquals(3, urlRepository.findAll().size());

        restTemplate.delete(baseUrl);

        List<UrlEntity> list = restTemplate.getForObject(baseUrl, List.class);

        assertEquals(0, list.size());
        assertEquals(3, urlRepository.findAll().size());
    }

    @Test
    void postUrlsAndGetAll(){

        String urlTest = "https://www.youtube.com/";

        UrlRequest urlRequest = UrlRequest.builder().url(urlTest).build();

        restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);
        restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);
        restTemplate.postForObject(baseUrl, urlRequest, UrlResponse.class);

        List<UrlEntity> list = restTemplate.getForObject(baseUrl, List.class);

        assertEquals(3, list.size());
    }

}
