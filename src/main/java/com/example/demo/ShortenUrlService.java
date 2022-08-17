package com.example.demo;

import com.example.demo.dao.UrlRepository;
import com.example.demo.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortenUrlService {

    @Autowired
    private UrlRepository repository;

    public Url saveUrl(Url url){
        return repository.save(url);
    }

    public List<Url> getUrls(){
        return repository.findAll();
    }

}
