package com.example.demo;

import com.example.demo.dao.UrlRepository;
import com.example.demo.model.UrlEntity;
import com.example.demo.model.UrlRequest;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShortenUrlService {

    int MAX_EXPIRATION_HOURS = 2;

    @Autowired
    private UrlRepository repository;

    public UrlEntity shortUrl(UrlRequest urlRequest){
        if(StringUtils.isNotEmpty(urlRequest.getUrl())){
            String encodedUrl = encodeUrl(urlRequest.getUrl());

            UrlEntity url = new UrlEntity();
            url.setCreationDate(LocalDateTime.now());
            url.setOriginalUrl(urlRequest.getUrl());
            url.setShortUrl(encodedUrl);
            url.setDeleted(false);
            return save(url);
        }
        return null;
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
        List<UrlEntity> urlList = repository.findByShortUrl(url);
        return  urlList.get(0);
    }

    public UrlEntity save(UrlEntity urlEntity){
        return repository.save(urlEntity);
    }

    public Optional<UrlEntity> findById(Integer id){
        return repository.findById(id);
    }

    public void softDeleteById(Integer id){
        Optional<UrlEntity> optionalUrl = findById(id);
        if(optionalUrl.isPresent()){
            UrlEntity url = optionalUrl.get();
            url.setDeleted(true);
            save(url);
        }
    }

    public void softDeleteAll() {
        repository.findAll().stream().forEach(url -> {
            url.setDeleted(true);
            save(url);
        });
    }

    public List<UrlEntity> findAll(){
        return repository.findAll().stream()
                            .filter(url -> !url.getDeleted())
                            .collect(Collectors.toList());
    }

}
