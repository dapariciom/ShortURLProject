package com.example.shorturl.service.url;

import com.example.shorturl.dao.UrlRepository;
import com.example.shorturl.model.payload.request.url.UrlRequest;
import com.example.shorturl.model.payload.request.url.UserUrlRequest;
import com.example.shorturl.model.url.UrlEntity;
import com.example.shorturl.service.sequence.SequenceGeneratorService;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.shorturl.model.url.UrlEntity.SEQUENCE_NAME;

@Service
public class ShortenUrlService implements IShortenUrlService {

    long EXPIRATION_TIME = 20;

    private final UrlRepository urlRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public ShortenUrlService(final UrlRepository urlRepository, final SequenceGeneratorService sequenceGeneratorService){
        this.urlRepository = urlRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public UrlEntity shortUrl(UrlRequest urlRequest) {

        String encodedUrl = encodeUrl(urlRequest.getUrl());

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        UrlEntity urlEntity = UrlEntity.builder()
                .id(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME))
                .shortUrl(encodedUrl)
                .completeShortUrl(baseUrl + "/api/redirect/" + encodedUrl)
                .originalUrl(urlRequest.getUrl())
                .creationDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusSeconds(TimeUnit.SECONDS.convert(EXPIRATION_TIME, TimeUnit.SECONDS)))
                .isDeleted(false)
                .isExpired(false)
                .build();

        return save(urlEntity);
    }

    public UrlEntity userShortUrl(Long userId, UserUrlRequest userUrlRequest) {

        String alias = userUrlRequest.getAlias();

        String encodedUrl = alias != null ? alias :
                encodeUrl(userUrlRequest.getUrl());

        Long expirationTime = userUrlRequest.getExpirationTime() != null ? userUrlRequest.getExpirationTime() :
                TimeUnit.SECONDS.convert(EXPIRATION_TIME, TimeUnit.SECONDS);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        UrlEntity urlEntity = UrlEntity.builder()
                .id(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME))
                .shortUrl(encodedUrl)
                .alias(userUrlRequest.getAlias())
                .createdBy(userId)
                .completeShortUrl(baseUrl + "/api/redirect/" + encodedUrl)
                .originalUrl(userUrlRequest.getUrl())
                .creationDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusSeconds(expirationTime))
                .isDeleted(false)
                .isExpired(false)
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

    public Optional<UrlEntity> getEncodedUrl(String shortUrl){
        Optional<UrlEntity> optionalUrl = urlRepository.findByShortUrl(shortUrl);

        if(optionalUrl.isPresent())
            optionalUrl.get().checkIfHasExpired();

        return optionalUrl;
    }

    private UrlEntity save(UrlEntity urlEntity){
        return urlRepository.save(urlEntity);
    }

    public Optional<UrlEntity> findByAliasAndCreatedBy(String alias, Long createdBy){

        Optional<UrlEntity> optionalUrl = urlRepository.findByAliasAndCreatedByAndIsDeletedAndIsExpired(alias, createdBy, false, false);

        if(optionalUrl.isPresent()) {
            optionalUrl.get().checkIfHasExpired();
            if (optionalUrl.get().getIsExpired()) {
                save(optionalUrl.get());
                return Optional.empty();
            }
        }

        return optionalUrl;
    }

    public List<UrlEntity> findByCreatedBy(Long createdBy){
        List<UrlEntity> urlList = urlRepository.findByCreatedByAndIsDeletedAndIsExpired(createdBy, false, false);
        return checkIfUrlsHaveExpired(urlList);
    }
    public Optional<UrlEntity> findByIdAndCreatedBy(Long id, Long createdBy){
        return urlRepository.findByIdAndCreatedBy(id, createdBy);
    }

    public Optional<UrlEntity> findById(Long id){
        Optional<UrlEntity> optionalUrl = urlRepository.findById(id);

        if(optionalUrl.isPresent())
            optionalUrl.get().checkIfHasExpired();

        return optionalUrl;
    }

    public void softDeleteById(Long id){
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

        List<UrlEntity> urlList = urlRepository.findAll();

        urlList.stream()
                .filter(url -> !url.getIsExpired())
                .forEach(UrlEntity::checkIfHasExpired);

        return urlList.stream()
                .filter(url -> !url.getIsDeleted())
                .collect(Collectors.toList());
    }

    public List<UrlEntity> checkIfUrlsHaveExpired(List<UrlEntity> list){
        list.stream().forEach(url -> {
                    url.checkIfHasExpired();
                    if(url.getIsExpired()) save(url);
                });

        return list.stream()
                .filter(url -> !url.getIsExpired())
                .collect(Collectors.toList());
    }

}
