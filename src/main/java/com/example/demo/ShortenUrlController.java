package com.example.demo;

import com.example.demo.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
public class ShortenUrlController {

    @Autowired
    private final ShortenUrlService shortenUrlService;

    @Autowired
    public ShortenUrlController(final ShortenUrlService shortenUrlService){
        this.shortenUrlService = shortenUrlService;
    }

    //TODO: ShortenUrl Logic
    @PostMapping("/url")
    public ResponseEntity<Void> saveUrl(@RequestBody Url url){
        shortenUrlService.save(url);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    //TODO: ShortenUrl Logic
    @GetMapping("/url/{id}")
    public ResponseEntity<Optional<Url>> findById(@PathVariable Integer id){
        Optional<Url> urlList = shortenUrlService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(urlList);
    }

    //TODO: ShortenUrl Logic
    @DeleteMapping("/url/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){
        shortenUrlService.deleteById(id);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    //TODO: ShortenUrl Logic
    @DeleteMapping("/url")
    public ResponseEntity<Void> deleteAll(){
        shortenUrlService.deleteAll();
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    //TODO: ShortenUrl Logic
    @GetMapping("/url")
    public ResponseEntity<List<Url>> getUrls(){
        List<Url> urlList = shortenUrlService.findAll();
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(urlList);
    }

}
