package com.example.demo;

import com.example.demo.dao.UrlRepository;
import com.example.demo.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShortenUrlService {

    @Autowired
    private UrlRepository repository;

    public Url save(Url url){
        return repository.save(url);
    }

    public Optional<Url> findById(Integer id){
        return repository.findById(id);
    }

    public void deleteById(Integer id){
        //TODO: Handle soft delete
        //repository.deleteById(id);
    }

    public void deleteAll(){
        //TODO: Handle soft delete
        //repository.deleteAll();
    }

    public List<Url> findAll(){
        return repository.findAll();
    }

}
