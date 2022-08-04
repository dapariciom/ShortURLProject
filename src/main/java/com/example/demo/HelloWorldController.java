package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping
    public String helloWorld(){
        return "Hello World from Spring Boot";
    }

    @GetMapping("/goodbye")
    public String goodbye(){
        return "Goodbye from Spring Boot";
    }

}
