package com.example.demo;

import com.example.demo.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShortenUrlController {

    @Autowired
    private ShortenUrlService shortenUrlService;

    @GetMapping
    public ResponseEntity<String> helloWorld(){
        String msg = "Hello World from Spring Boot";
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(msg,headers, HttpStatus.OK);
    }

    @PostMapping("/url")
    public ResponseEntity<Void> saveUrl(@RequestBody Url url){
        shortenUrlService.saveUrl(url);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    @GetMapping("/url")
    public ResponseEntity<List<Url>> getUrls(){
        List<Url> urlList = shortenUrlService.getUrls();
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(urlList);
    }

}
