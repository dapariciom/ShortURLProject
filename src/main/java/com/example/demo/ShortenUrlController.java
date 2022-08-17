package com.example.demo;

import com.example.demo.dao.UrlRepository;
import com.example.demo.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShortenUrlController {

    @Autowired
    private UrlRepository repository;

    @GetMapping
    public String helloWorld(){
        return "Hello World from Spring Boot";
    }

    @PostMapping("/url")
    public Url saveUrl(@RequestBody Url url){

        //TODO: Use ResponseEntity
        return repository.save(url);
    }

    @GetMapping("/url")
    public List<Url> getUrls(){

        //TODO: Use ResponseEntity
        return repository.findAll();
    }

}
