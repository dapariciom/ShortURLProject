package com.example.shorturl.service;

import com.example.shorturl.dao.UrlRepository;
import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.model.UrlRequest;
import com.example.shorturl.service.sequence.SequenceGeneratorService;
import com.example.shorturl.utils.UrlNotFoundException;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.shorturl.model.UrlEntity.SEQUENCE_NAME;

@Service
public class ShortenUrlService implements IShortenUrlService{

    long EXPIRATION_TIME_HOURS = 1;

    private final UrlRepository urlRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public ShortenUrlService(final UrlRepository urlRepository, final SequenceGeneratorService sequenceGeneratorService){
        this.urlRepository = urlRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public Optional<UrlEntity> shortUrl(UrlRequest urlRequest) {

        String encodedUrl = encodeUrl(urlRequest.getUrl());

        UrlEntity urlEntity = UrlEntity.builder()
                .id(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME))
                .shortUrl(encodedUrl)
                //TODO: Make dynamic
                .completeShortUrl("http://localhost:8080/api/v1/url/redirect/" + encodedUrl)
                .originalUrl(urlRequest.getUrl())
                .creationDate(LocalDateTime.now())
                //TODO: Logic to set expiration time
                .expirationDate(LocalDateTime.now().plusHours(EXPIRATION_TIME_HOURS))
                .isDeleted(false)
                .build();

        return save(urlEntity);
    }

    private String encodeUrl(String url){
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }

    public UrlEntity getEncodedUrl(String url){
        List<UrlEntity> urlList = urlRepository.findByShortUrl(url);
        return  urlList.get(0);
    }

    private Optional<UrlEntity> save(UrlEntity urlEntity){
        return Optional.ofNullable(urlRepository.save(urlEntity));
    }

    public Optional<UrlEntity> findById(Integer id){
        return urlRepository.findById(id);
    }

    public void softDeleteById(Integer id){
        Optional<UrlEntity> optionalUrl = findById(id);
        if(optionalUrl.isPresent()){
            UrlEntity url = optionalUrl.get();
            url.setIsDeleted(true);
            save(url);
        }
    }

    public void softDeleteAll() {
        urlRepository.findAll().stream().forEach(url -> {
            url.setIsDeleted(true);
            save(url);
        });
    }

    public List<UrlEntity> findAll(){
        return urlRepository.findAll().stream()
                            .filter(url -> !url.getIsDeleted())
                            .collect(Collectors.toList());
    }

}
